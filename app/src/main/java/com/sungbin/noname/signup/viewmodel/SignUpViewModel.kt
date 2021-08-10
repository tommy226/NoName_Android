package com.sungbin.noname.signup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sungbin.noname.signup.repository.SignUpRepository
import com.sungbin.noname.util.Event
import com.sungbin.noname.util.customEnqueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private val TAG = SignUpViewModel::class.java.simpleName
    private val repo = SignUpRepository()

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    private val _isRegister = MutableLiveData<Boolean>(false)
    val isRegister: LiveData<Boolean>
        get() = _isRegister

    val inputAccount = MutableLiveData<String>()
    val inputPW = MutableLiveData<String>()
    val inputPWcheck = MutableLiveData<String>()
    val inputName = MutableLiveData<String>()

    private var isAccountAbled: Boolean? = false

    private val _toast = MutableLiveData<Event<String>>()
    val toast: LiveData<Event<String>>
        get() = _toast

    private val _isCancel = MutableLiveData<Boolean>()
    val isCancel: LiveData<Boolean>
        get() = _isCancel
    fun cancel() = _isCancel.postValue(true)   // 회원가입 취소 버튼

    fun accountDuplicated() =
        viewModelScope.launch {                                        // 아이디 중복 확인
            val pattern = android.util.Patterns.EMAIL_ADDRESS
            val emailpattern = inputAccount.value?.let { pattern.matcher(it).matches() }
            if (emailpattern == true) {
                val response = inputAccount.value?.let { repo.accountDupCheck(it) }
                response?.customEnqueue(
                    onSuccess = {
                        isAccountAbled = true
                        _toast.value = Event("사용 가능한 아이디 입니다.")
                    },
                    onError = {
                        when {
                            it.code() == 409 -> {
                                isAccountAbled = false
                                _toast.value = Event("중복 된 아이디 입니다.")
                            }
                            else -> {
                                _toast.value = Event("${it.code()}")
                            }
                        }
                    }
                )
            } else _toast.value = Event("이메일 형식이 아닙니다.")
        }

    fun registerRequest(account: String, password: String, name: String) =
        viewModelScope.launch {      // 회원가입 요청
            if (blankCheck() && isPasswordAbled.value == true && isAccountAbled == true) {
                val response = repo.register(account, password, name)
                response.customEnqueue(
                    onSuccess = {
                        _toast.value = Event("회원 가입이 완료 되었습니다.")
                        _isRegister.value = true
                    },
                    onError = {
                        _toast.value = Event("회원 가입이 거절 되었습니다.")
                        _isRegister.value = false
                    },
                    onFailure = {
                        _toast.value = Event("Server error")
                        _isRegister.value = false
                    }
                )
            } else {
                _toast.value = Event("조건에 맞지 않습니다 다시 확인 해주세요")    // 클라이언트 입장에서 회원가입 요청 조건이 모두 맞는지 확인
                _isRegister.value = false
            }
        }

    private fun blankCheck() = !(inputAccount.value.isNullOrBlank()     // 회원가입 EditText Null 여부
            || inputPW.value.isNullOrEmpty()
            || inputPWcheck.value.isNullOrEmpty()
            || inputName.value.isNullOrBlank())

    private val _isPasswordAbled = MediatorLiveData<Boolean>().apply { // 비밀번호 동일한지 확인
        addSource(inputPW){ value = pwCheck() }
        addSource(inputPWcheck){ value = pwCheck() }
    }
    val isPasswordAbled: LiveData<Boolean>
        get() = _isPasswordAbled
    private fun pwCheck() = inputPW.value.equals(inputPWcheck.value)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}