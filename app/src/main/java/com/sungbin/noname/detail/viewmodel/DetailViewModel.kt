package com.sungbin.noname.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sungbin.noname.home.data.Board
import com.sungbin.noname.home.data.FileDto
import com.sungbin.noname.home.repository.SharedRepository
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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}