package com.sungbin.noname.home.repository

import com.sungbin.noname.App
import com.sungbin.noname.network.ServerImpl
import com.sungbin.noname.util.PreferenceUtil

class SharedRepository {
    private val token = App.prefs.getString(PreferenceUtil.AccessToken, "")
    private val id = App.prefs.getString(PreferenceUtil.myId, "")

    fun profileImageGet() = ServerImpl.service.profileImageGet(token = token, memberId = id)

}