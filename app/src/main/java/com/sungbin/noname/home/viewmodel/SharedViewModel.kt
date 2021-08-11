package com.sungbin.noname.home.viewmodel

import androidx.lifecycle.ViewModel
import com.sungbin.noname.home.repository.SharedRepository
import com.sungbin.noname.util.customEnqueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val TAG = SharedViewModel::class.java.simpleName
    private val repo = SharedRepository()

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    fun getImage() = viewModelScope.launch {
        val response = repo.profileImageGet()

        response.customEnqueue(
            onSuccess = {},
            onError = {},
            onFailure = {}
        )
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}