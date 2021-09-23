package com.sungbin.noname.detail.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sungbin.noname.home.data.Board
import com.sungbin.noname.home.data.FileDto
import com.sungbin.noname.home.data.Subscribe
import com.sungbin.noname.home.repository.SharedRepository
import com.sungbin.noname.util.Event
import com.sungbin.noname.util.ListLivedata
import com.sungbin.noname.util.customEnqueue
import com.sungbin.noname.util.toComma
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val TAG = DetailViewModel::class.java.simpleName
    private val repo = SharedRepository()

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    var boardId: Int? = null

    private var _detailResponse = MutableLiveData<Board>()
    val detailResponse: LiveData<Board>
        get() = _detailResponse
    val emptyList = listOf<FileDto>()

    private var _isLiked = MutableLiveData<Event<Boolean>>()
    val isLiked: LiveData<Event<Boolean>>
        get() = _isLiked

    private var _likeCount = MutableLiveData<String>("")
    val likeCount: LiveData<String>
        get() = _likeCount

    private var templikeCount = 0

    private var _isDelete = MutableLiveData<Event<Boolean>>()
    val isDelete: LiveData<Event<Boolean>>
        get() = _isDelete

    private var _isDetailLikes = MutableLiveData<Event<Boolean>>()
    val isDetailLikes: LiveData<Event<Boolean>>
        get() = _isDetailLikes
    fun getDetailLikes(){
        _isDetailLikes.value = Event(true)
    }

    // 게시물 좋아요 , 유저 팔로우 상세 정보
    var detailSubscribes = ListLivedata<Subscribe>()
    fun clearSubscribes() = detailSubscribes.clear()
    val emptySubscribes = listOf<Subscribe>()

    fun getBoardDetail(id: Int) = viewModelScope.launch {
        val response = repo.getBoard(id)

        response.customEnqueue(
            onSuccess = {
                        if(it.code()==200) _detailResponse.value = it.body()?.items?.board
            },
            onError = {},
            onFailure = {}
        )
    }

    fun likeBoard(boardId: Int) = viewModelScope.launch {
        val response = repo.likeByBoard(boardId = boardId)

        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200) {
                    _isLiked.value = Event(true)
                    _detailResponse.value?.fallow?.id = it.body()?.items?.id!!  // 좋아요 할 시 subscribeId 업데이트
                    templikeCount += 1
                    _likeCount.value = templikeCount.toComma()
                }
            },
            onError = {},
            onFailure = {}
        )
    }

    fun unlikeBoard(subscribeId: Int) = viewModelScope.launch {
        val response = repo.unSubscribe(subscribeId = subscribeId)

        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200){
                    _isLiked.value = Event(false)
                    templikeCount -= 1
                    _likeCount.value = templikeCount.toComma()
                }
            },
            onError = {},
            onFailure = {}
        )
    }

    fun getlikeCount(boardId: Int) = viewModelScope.launch {
        val response = repo.getSubscribePageByBoardId(boardId)

        response.customEnqueue(
            onSuccess = {
                        if(it.code()==200){
                            templikeCount = it.body()?.items?.TotalElements ?: 0
                            _likeCount.value = templikeCount.toComma()
                        }
            },
            onError = {},
            onFailure = {}
        )
    }

    fun deleteMyBoard(boardId: Int) = viewModelScope.launch {
        val response = repo.deleteBoard(boardId)

        response.customEnqueue(
            onSuccess = {
                        if(it.code() == 200){
                            _isDelete.value = Event(true)
                        }
            },
            onError = {},
            onFailure = {}
        )
    }

    fun getDetailFallow(ownerId: Int) = viewModelScope.launch {
        val response = repo.getSubscribePageByOwenerId(ownerId)

        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200) {
                    it.body()?.items?.subscribes?.toMutableList()?.let { subscribes ->
                        detailSubscribes.addAll(subscribes)
                    }
                }
            },
            onError = {},
            onFailure = {}
        )
    }

    fun getDetailLike(boardId: Int) = viewModelScope.launch {
        val response = repo.getSubscribePageByBoardId(boardId)

        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200) {
                    it.body()?.items?.subscribes?.toMutableList()?.let { subscribes ->
                        detailSubscribes.addAll(subscribes)
                    }
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