package com.sungbin.noname.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sungbin.noname.App
import com.sungbin.noname.home.data.Board
import com.sungbin.noname.home.data.FeedPagingResponse
import com.sungbin.noname.home.data.GetProfileImageResponse
import com.sungbin.noname.home.data.MemberDto
import com.sungbin.noname.home.repository.SharedRepository
import com.sungbin.noname.util.Event
import com.sungbin.noname.util.ListLivedata
import com.sungbin.noname.util.PreferenceUtil
import com.sungbin.noname.util.customEnqueue
import kotlinx.coroutines.*

open class SharedViewModel : ViewModel() {
    private val TAG = SharedViewModel::class.java.simpleName
    private val repo = SharedRepository()

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    // 타이틀 바
    var titleName = MutableLiveData<String>("")

    // 자신의 Id
    val myId: Int = App.prefs.getInteger(PreferenceUtil.myId, 99999)

    // 자신의 계정
    private var _myAccount = MutableLiveData<String>("")
    val myAccount: LiveData<String>
    get() = _myAccount

    // 자신의 이름
    private var _myName = MutableLiveData<String>("")
    val myName: LiveData<String>
    get() = _myName

    // 자신의 소개글
    private var _myInfo = MutableLiveData<String>("")
    val myInfo: LiveData<String>
        get() = _myInfo

    // 자신의 프로필 이미지
    private var _myImage = MutableLiveData<String>("")
    val myImage: LiveData<String>
        get() = _myImage

    // 게시물(피드) 데이터
    private var _feedResponse = MutableLiveData<FeedPagingResponse>()
    val feedResponse: LiveData<FeedPagingResponse>
        get() = _feedResponse

    // 게시물 페이지
    var page = 0

    // 유저가 올린 게시글
    var userBoards = ListLivedata<Board>()
    var userBoardsCount = MutableLiveData(0)
    fun clearBoards(){
        userBoards.clear()
    }

    private var _likeSuccess = MutableLiveData<Event<Boolean>>()
    val likeSuccess: LiveData<Event<Boolean>>
        get() = _likeSuccess

    fun getInfo(id: Int) = viewModelScope.launch {
        val response = repo.getInfo(id)

        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200) {
                    _myAccount.value = it.body()?.items?.member?.account
                    _myName.value = it.body()?.items?.member?.name
                    _myInfo.value = it.body()?.items?.member?.info
                    _myImage.value = it.body()?.items?.src
                }

            },
            onError = {

            },
            onFailure = {

            }
        )
    }

    fun getBoards(number: Int) = viewModelScope.launch {
        val response = repo.getBoards(number)

        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200) _feedResponse.value = it.body()
            },
            onError = {},
            onFailure = {}
        )
    }

    fun getBoardsMember(memberId: Int, number: Int) = viewModelScope.launch {
        val response = repo.getUserBoards(memberId, number)

        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200) {
                    it.body()?.items?.boards?.toMutableList()?.let { boards ->
                        userBoards.addAll(boards)
                    }
                    userBoardsCount.value = it.body()?.items?.TotalElements
                }
            },
            onError = {},
            onFailure = {}
        )
    }

    fun getBoard(id: Int) = viewModelScope.launch {
        val response = repo.getBoard(id)

        response.customEnqueue(
            onSuccess = {},
            onError = {},
            onFailure = {}
        )
    }

    fun getProfileImage(id: Int) = viewModelScope.launch {
        val response = repo.getProfileImage(id)

        response.customEnqueue(
            onSuccess = {},
            onError = {},
            onFailure = {}
        )
    }

    fun fallow(ownerId: Int) = viewModelScope.launch {
        val response = repo.fallowByMember(ownerId = ownerId)

        response.customEnqueue(
            onSuccess = {},
            onError = {},
            onFailure = {}
        )
    }

    fun likeBoard(boardId: Int) = viewModelScope.launch {
        val response = repo.likeByBoard(boardId = boardId)

        response.customEnqueue(
            onSuccess = {
                        if(it.code() == 200){

                        }
            },
            onError = {},
            onFailure = {}
        )
    }

    fun unSubscribe(subscribeId: Int) = viewModelScope.launch {
        val response = repo.unSubscribe(subscribeId = subscribeId)

        response.customEnqueue(
            onSuccess = {},
            onError = {},
            onFailure = {}
        )
    }

    fun getSubscribeOwnerId(ownerId: Int) = viewModelScope.launch {
        val response = repo.getSubscribePageByOwenerId(ownerId)

        response.customEnqueue(
            onSuccess = {},
            onError = {},
            onFailure = {}
        )
    }

    fun getSubscribeBoardId(boardId: Int) = viewModelScope.launch {
        val response = repo.getSubscribePageByBoardId(boardId)

        response.customEnqueue(
            onSuccess = {},
            onError = {},
            onFailure = {}
        )
    }

    fun getlikeCount(boardId: Int) {
        val response = repo.getSubscribePageByBoardId(boardId)
        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200) {

                }
            },
            onError = {},
            onFailure = {}
        )
    }



    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}