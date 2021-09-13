package com.sungbin.noname.home.repository

import com.sungbin.noname.App
import com.sungbin.noname.home.data.SubscribeRequest
import com.sungbin.noname.network.ServerImpl
import com.sungbin.noname.util.PreferenceUtil

class SharedRepository {
    private val token = App.prefs.getString(PreferenceUtil.AccessToken, "")

    fun fallowByMember(ownerId: Int) = ServerImpl.service.subscribe(token = token, SubscribeRequest(ownerId = ownerId, boardId = 0))

    fun likeByBoard(boardId: Int) = ServerImpl.service.subscribe(token = token, SubscribeRequest(ownerId = 0, boardId = boardId))

    fun getProfileImage(id: Int) = ServerImpl.service.getProfileImage(token = token, memberId = id)

    fun getInfo(id: Int) = ServerImpl.service.getInfo(token = token, memberId = id)

    fun getBoard(id: Int) = ServerImpl.service.getBoard(token = token, boardId = id)

    fun getBoards(number: Int) = ServerImpl.service.getBoards(token = token, page = number)

    fun getUserBoards(memberId: Int, number: Int) = ServerImpl.service.getUserBoards(token = token, memberId = memberId, page = number)

    fun getSubscribePageByOwenerId(ownerId: Int) = ServerImpl.service.getSubscribePageByOwenerId(token = token, owenerId = ownerId)

    fun getSubscribePageByBoardId(boardId: Int) = ServerImpl.service.getSubscribePageByBoardId(token = token, boardId = boardId)
}