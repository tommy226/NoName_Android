package com.sungbin.noname.home.repository

import com.sungbin.noname.App
import com.sungbin.noname.network.ServerImpl
import com.sungbin.noname.util.PreferenceUtil

class SharedRepository {
    private val token = App.prefs.getString(PreferenceUtil.AccessToken, "")
    private val id = App.prefs.getString(PreferenceUtil.myId, "")

    fun getProfileImage() = ServerImpl.service.getProfileImage(token = token, memberId = id)

    fun getInfo() = ServerImpl.service.getInfo(token = token, memberId = id)

}