package com.sungbin.noname.upload.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sungbin.noname.upload.repository.UploadRepository
import com.sungbin.noname.util.Event
import com.sungbin.noname.util.customEnqueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UploadEditViewModel: ViewModel() {
    private val TAG = UploadViewModel::class.java.simpleName
    private val repo = UploadRepository()

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    var boardId: Int? = -1

    val myName = MutableLiveData<String>("")
    val myImageUrl = MutableLiveData<String>("")
    val inputContent = MutableLiveData<String>("")

    private var _isEdit = MutableLiveData<Event<Boolean>>()
    val isEdit: LiveData<Event<Boolean>>
        get() = _isEdit

    fun editBoard(boardId: Int, content: String) = viewModelScope.launch {
        val response = repo.editBoard(boardId, content)

        response.customEnqueue(
            onSuccess = {
                        if(it.code() == 200) _isEdit.value = Event(true)
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