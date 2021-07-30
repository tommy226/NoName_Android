package com.sungbin.noname.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val TAG = SharedViewModel::class.java.simpleName
    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    val _mutablelivedata = MutableLiveData<Int>()

    val mutableLiveData: LiveData<Int>
        get() = _mutablelivedata

    fun add() {
        viewModelScope.launch {
            _mutablelivedata.value = (mutableLiveData.value ?: 0).plus(2)
        }
    }
}