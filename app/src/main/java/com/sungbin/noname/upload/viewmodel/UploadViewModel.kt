package com.sungbin.noname.upload.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sungbin.noname.upload.data.BoardsContentResponse
import com.sungbin.noname.upload.repository.UploadRepository
import com.sungbin.noname.util.Event
import com.sungbin.noname.util.customEnqueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel : ViewModel() {
    private val TAG = UploadViewModel::class.java.simpleName
    private val repo = UploadRepository()

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    val inputContent = MutableLiveData<String>("")      // uploadBoard의 content 파라미터 (게시글내용)

    private val _toast = MutableLiveData<Event<String>>()
    val toast: LiveData<Event<String>>
        get() = _toast

    private val _boardID = MutableLiveData<BoardsContentResponse>()
    val boardId: LiveData<BoardsContentResponse>
        get() = _boardID

    fun uploadContent(content: String) = viewModelScope.launch {
        val response = repo.uploadContent(content)
        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200) _boardID.value = it.body()
            },
            onError = {
                _toast.value = Event("서버에 문제가 있습니다. 다시 시도해주세요")
            },
            onFailure = {
                _toast.value = Event("서버에 문제가 있습니다. 다시 시도해주세요")
            }
        )
    }

    fun uploadFiles(
        files: MutableList<MultipartBody.Part>,
//        data: HashMap<String, RequestBody>
        id: String
    ) = viewModelScope.launch {
        val response = repo.uploadFiles(files, id)

        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200) _toast.value = Event("업로드 성공")
            },
            onError = {
                _toast.value = Event("서버에 문제가 있습니다. 다시 시도해주세요")
            },
            onFailure = {
                _toast.value = Event("서버에 문제가 있습니다. 다시 시도해주세요")
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}