package com.sungbin.noname.login.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sungbin.noname.App
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityLoginBinding
import com.sungbin.noname.home.ui.HomeActivity
import com.sungbin.noname.login.viewmodel.LoginViewModel
import com.sungbin.noname.signup.ui.SignUpActivity
import com.sungbin.noname.util.PreferenceUtil
import com.sungbin.noname.util.showToast

class LoginActivity : AppCompatActivity() {
    private val TAG = LoginActivity::class.java.simpleName

    private val viewmmodel: LoginViewModel by viewModels()

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.run {
            vm = viewmmodel
            lifecycleOwner = this@LoginActivity
        }
        autoLogin()

        viewmmodel.tokenModel.observe(this, Observer { tokenModel ->
            Log.d(TAG, tokenModel.toString())                                                           // 서버에서 전달 받은 Token
            App.prefs.setString(PreferenceUtil.AccessToken, tokenModel.accessToken)
            App.prefs.setString(PreferenceUtil.RefreshToken, tokenModel.refreshToken)
        })
        viewmmodel.loginData.observe(this, Observer { userData ->
            App.prefs.setString(PreferenceUtil.Name, userData.items.name)
            App.prefs.setString(PreferenceUtil.myId, userData.items.id)
        })

        viewmmodel.loginResult.observe(this, Observer { result ->
            Log.d(TAG, "LOGIN RESULT : ${result}")

            if (result) {
                showToast("로그인 성공")
                if (!viewmmodel.inputAccount.value.isNullOrEmpty()) {
                    App.prefs.setString(PreferenceUtil.Account, viewmmodel.inputAccount.value!!)      // 로그인 성공 시 자동로그인 아이디 등록
                    App.prefs.setString(PreferenceUtil.Password, viewmmodel.inputPW.value!!)
                }
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else showToast("로그인 실패")
        })

        viewmmodel.registerFlag.observe(this, Observer { result ->                                // 회원가입
            if(result){
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
                viewmmodel.registerFlagDone()
            }
        })
    }

    fun autoLogin() {
        if (App.prefs.getString(PreferenceUtil.Account, "").isNotEmpty()) {
            val account = App.prefs.getString(PreferenceUtil.Account, "")
            val password = App.prefs.getString(PreferenceUtil.Password, "")
            viewmmodel.loginRequest(account, password)
        }
    }

    override fun onRestart() {
        super.onRestart()
        autoLogin()
    }
}