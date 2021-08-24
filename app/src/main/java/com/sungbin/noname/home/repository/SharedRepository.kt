package com.sungbin.noname.home.repository

import com.sungbin.noname.App
import com.sungbin.noname.network.ServerImpl
import com.sungbin.noname.util.PreferenceUtil

class SharedRepository {
    private val token = App.prefs.getString(PreferenceUtil.AccessToken, "")
    private val myId = App.prefs.getString(PreferenceUtil.myId, "")

    fun getProfileImage(id: String) = ServerImpl.service.getProfileImage(token = token, memberId = id)

    fun getInfo() = ServerImpl.service.getInfo(token = token, memberId = myId)

    fun getBoard() = ServerImpl.service.getBoard(token = token, boardId = "57")

    fun getBoards(number: Int) = ServerImpl.service.getBoards(token = token, page = number)

    fun getUserBoards(memberId: String, number: Int) = ServerImpl.service.getUserBoards(token = token, memberId = memberId, page = number)
}