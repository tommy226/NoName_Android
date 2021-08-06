package com.sungbin.noname.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sungbin.noname.login.data.LoginResponse
import com.sungbin.noname.login.data.LoginToken
import com.sungbin.noname.login.repository.LoginRepository
import com.sungbin.noname.util.customEnqueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val TAG = LoginViewModel::class.java.simpleName
    private val repo = LoginRepository()

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    private val _tokenModel = MutableLiveData<LoginToken>()
    val tokenModel: LiveData<LoginToken>
        get() = _tokenModel

    private val _loginData = MutableLiveData<LoginResponse>()
    val loginData: LiveData<LoginResponse>
        get() = _loginData

    val inputAccount = MutableLiveData<String>("")
    val inputPW = MutableLiveData<String>("")

    private val _registerFlag = MutableLiveData<Boolean>()
    val registerFlag: LiveData<Boolean>
        get() = _registerFlag
    fun registerFlagDone() = _registerFlag.postValue(false)

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    fun loginRequest(
        account: String,
        password: String
    )                                                     // 로그인 요청
            = viewModelScope.launch {
        val response = repo.login(account, password)
        response.customEnqueue(
            onSuccess = {
                val accessToken = it.headers()["accessToken"]
                val refreshToken = it.headers()["refreshToken"]
                Log.d(TAG, "MY accessToken : ${it.headers()["accessToken"]}")
                Log.d(
                    TAG,
                    "MY refreshToken : ${it.headers()["refreshToken"]}"
                )
                _tokenModel.value = LoginToken(
                    accessToken = accessToken.toString(),
                    refreshToken = refreshToken.toString()     // 로그인 성공 시 accessToken, refreshToken 획득
                )
                _loginData.value = it.body()
                _loginResult.value = true
            },
            onError = {
                _loginResult.value = false
            }
        )
    }

    fun callRegister() = _registerFlag.postValue(true)

    private val _isEndalbedLogin = MediatorLiveData<Boolean>().apply {
        addSource(inputAccount){ value = isValidEnterInfo() }
        addSource(inputPW){ value = isValidEnterInfo() }
    }
    val isEndalbedLogin: LiveData<Boolean>
        get() = _isEndalbedLogin

    private fun isValidEnterInfo() = !inputAccount.value.isNullOrBlank() && !inputPW.value.isNullOrEmpty()
    // 아이디, 비밀번호가 모두 입력 되었을 시 버튼 활성화

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}