package com.sungbin.noname.signup.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sungbin.noname.MyApplication
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityRegisterBinding
import com.sungbin.noname.signup.viewmodel.SignUpViewModel
import com.sungbin.noname.util.EventObserver
import com.sungbin.noname.util.Patterns
import com.sungbin.noname.util.showToast

class SignUpActivity : AppCompatActivity(){
    private val TAG = SignUpActivity::class.java.simpleName

    private val viewmmodel: SignUpViewModel by viewModels()

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.run {
            vm = viewmmodel
            lifecycleOwner = this@SignUpActivity
        }
        viewmmodel.toast.observe(this, EventObserver { message ->
            showToast(message)
        })

        viewmmodel.isPasswordAbled.observe(this, Observer {  pwCheck ->
            if(pwCheck) Log.d(TAG, "패스워드 사용 가능") else Log.d(TAG, "패스워드 사용 불가")
        })

        viewmmodel.isCancel.observe(this, Observer { isCancel ->
            if(isCancel) finish()
        })

        viewmmodel.isRegister.observe(this, Observer { isRegister ->
            if(isRegister){
                MyApplication.prefs.setString("account", viewmmodel.inputAccount.value!!)           // 로그인 성공 시 자동로그인 아이디 등록
                MyApplication.prefs.setString("password", viewmmodel.inputPW.value!!)
                finish()
            }
        })

        viewmmodel.inputAccount.observe(this, Observer {  account ->    // 이메일 조건 정규식
            val pattern = Patterns.ePattern.matcher(account)
            if (!account.isNullOrBlank()) {
                if (pattern.matches()) {
                    binding.accountRightText.visibility = View.VISIBLE
                    binding.accountErrorText.visibility = View.GONE
                } else {
                    binding.accountRightText.visibility = View.GONE
                    binding.accountErrorText.visibility = View.VISIBLE
                }
            } else {
                binding.accountRightText.visibility = View.GONE
                binding.accountErrorText.visibility = View.GONE
            }
        })

        viewmmodel.inputPW.observe(this, Observer { password ->         // 패스워드 조건 정규식
            val pattern = Patterns.pPattern.matcher(password)
            if (!password.isNullOrBlank()) {
                if (pattern.matches()) {
                    binding.passwordRightText.visibility = View.VISIBLE
                    binding.passwordErrorText.visibility = View.GONE
                } else {
                    binding.passwordRightText.visibility = View.GONE
                    binding.passwordErrorText.visibility = View.VISIBLE
                }
            } else {
                binding.passwordRightText.visibility = View.GONE
                binding.passwordErrorText.visibility = View.GONE
            }
        })
    }
}