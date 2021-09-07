package com.sungbin.noname

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.sungbin.noname.util.PreferenceUtil

class App : Application() {
    companion object{
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        super.onCreate()
        prefs = PreferenceUtil(applicationContext)

        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}