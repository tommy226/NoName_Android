package com.sungbin.noname

import android.app.Application
import com.sungbin.noname.util.PreferenceUtil

class MyApplication : Application() {
    companion object{
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}