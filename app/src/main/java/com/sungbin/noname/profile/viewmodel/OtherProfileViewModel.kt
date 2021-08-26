package com.sungbin.noname.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.sungbin.noname.home.repository.SharedRepository
import com.sungbin.noname.util.customEnqueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OtherProfileViewModel : ViewModel() {
    private val TAG = OtherProfileViewModel::class.java.simpleName
    private val repo = SharedRepository()

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    fun getInfo(id: Int) = viewModelScope.launch {
        val response = repo.getInfo(id)
        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200) {
                }
            },
            onError = {

            },
            onFailure = {

            }
        )
    }

}