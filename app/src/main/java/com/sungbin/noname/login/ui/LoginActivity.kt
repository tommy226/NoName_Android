package com.sungbin.noname.login.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.sungbin.noname.App
import com.sungbin.noname.R
import com.sungbin.noname.databinding.ActivityHomeBinding
import com.sungbin.noname.databinding.ActivityLoginBinding
import com.sungbin.noname.home.ui.HomeActivity
import com.sungbin.noname.login.viewmodel.LoginViewModel
import com.sungbin.noname.signup.ui.SignUpActivity
import com.sungbin.noname.util.PreferenceUtil
import com.sungbin.noname.util.showToast


class LoginActivity : AppCompatActivity() {
    private val TAG = LoginActivity::class.java.simpleName

    private val viewModel: LoginViewModel by viewModels()

    private val binding: ActivityLoginBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_login)
    }
    private val googleSignInIntent by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(this, gso).signInIntent
    }

    private val GOOGLE_CODE = 10

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.apply {
            vm = viewModel
            lifecycleOwner = this@LoginActivity
        }
        binding.apply {
            googleLoginBtn.setOnClickListener {  googleLogin()  }
            kakaoLoginBtn.setOnClickListener { kakaoLogin() }
        }

        auth = FirebaseAuth.getInstance()

        autoLogin()

        viewModel.tokenModel.observe(this, Observer { tokenModel ->
            Log.d(TAG, tokenModel.toString())                                                           // 서버에서 전달 받은 Token
            App.prefs.setString(PreferenceUtil.AccessToken, tokenModel.accessToken)
            App.prefs.setString(PreferenceUtil.RefreshToken, tokenModel.refreshToken)
        })
        viewModel.loginData.observe(this, Observer { userData ->                                 // 자주 쓰이는 데이터 쉐어드 프리퍼런스에 임시 저장
            App.prefs.setString(PreferenceUtil.Name, userData.items.name)
            App.prefs.setInteger(PreferenceUtil.myId, userData.items.id)
        })

        viewModel.loginResult.observe(this, Observer { result ->
            Log.d(TAG, "LOGIN RESULT : ${result}")

            if (result) {
                showToast("로그인 성공")
                if (!viewModel.inputAccount.value.isNullOrEmpty()) {
                    App.prefs.setString(PreferenceUtil.Account, viewModel.inputAccount.value!!)      // 로그인 성공 시 자동로그인 아이디 등록
                    App.prefs.setString(PreferenceUtil.Password, viewModel.inputPW.value!!)
                }
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else showToast("로그인 실패")
        })

        viewModel.registerFlag.observe(this, Observer { result ->                                // 회원가입
            if(result){
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
                viewModel.registerFlagDone()
            }
        })
    }

    fun autoLogin() {
        if (App.prefs.getString(PreferenceUtil.Account, "").isNotEmpty()) {
            val account = App.prefs.getString(PreferenceUtil.Account, "")
            val password = App.prefs.getString(PreferenceUtil.Password, "")
            viewModel.loginRequest(account, password)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        Log.d(TAG, "is google user? : ${currentUser?.displayName}")
    }

    fun googleLogin(){
        startActivityForResult(googleSignInIntent, GOOGLE_CODE)
    }

    fun kakaoLogin(){
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
            UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity, callback = kakaoCallback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = kakaoCallback)
        }
    }
    internal val kakaoCallback : (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.d(TAG, "로그인 실패")
        } else if (token != null) {
            UserApiClient.instance.me { user, error ->
                Log.d(TAG, "kakaoId : ${user?.id}")
                Log.d(TAG, "kakaogroupUserToken : ${user?.groupUserToken}")
                Log.d(TAG, "kakaoAccount : ${user?.kakaoAccount}")
            }
            Log.d(TAG, "로그인 성공")
        }
    }

    override fun onRestart() {
        super.onRestart()
        autoLogin()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == GOOGLE_CODE){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)

                Log.d(TAG, "GOOGLE displayName : ${account.displayName}")
                Log.d(TAG, "GOOGLE e-mail : ${account.email}")
                Log.d(TAG, "GOOGLE idToken : ${account.idToken}")
                Log.d(TAG, "GOOGLE id : ${account.id}")
                Log.d(TAG, "GOOGLE isExpired : ${account.isExpired}")
                Log.d(TAG, "GOOGLE serverAuthCode : ${account.serverAuthCode}")



                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    showToast("구글 로그인에 실패 하였습니다.")
                }
            }
    }

    private fun googleLogout(){
        auth.signOut()
        Log.d(TAG, "Google Logout Success")
    }
}