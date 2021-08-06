package com.sungbin.noname.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    companion object{
        val Empty: String = ""

        val AccessToken: String = "access"
        val RefreshToken: String = "refresh"

        val Account: String = "account"
        val Password: String = "password"
        val Name: String = "name"
        val info: String = "info"
    }
    private val prefs: SharedPreferences =
        context.getSharedPreferences("AUTO_LOGIN", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str)
            .apply()
    }

    fun remove() {
        prefs.edit()
            .clear()
            .apply()
    }
}
