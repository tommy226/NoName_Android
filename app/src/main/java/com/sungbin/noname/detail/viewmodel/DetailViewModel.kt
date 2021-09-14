package com.sungbin.noname.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sungbin.noname.home.data.Board
import com.sungbin.noname.home.data.FileDto
import com.sungbin.noname.home.repository.SharedRepository
import com.sungbin.noname.util.Event
import com.sungbin.noname.util.customEnqueue
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
                }
            },
            onError = {},
            onFailure = {}
        )
    }

    fun unSubscribe(subscribeId: Int) = viewModelScope.launch {
        val response = repo.unSubscribe(subscribeId = subscribeId)

        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200){
                    _isLiked.value = Event(false)
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