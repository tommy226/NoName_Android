package com.sungbin.noname.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sungbin.noname.home.data.Board
import com.sungbin.noname.home.data.MemberResponse
import com.sungbin.noname.home.repository.SharedRepository
import com.sungbin.noname.util.ListLivedata
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

    private var _memberInfo = MutableLiveData<MemberResponse>()
    val memberInfo: LiveData<MemberResponse>
        get() = _memberInfo

    // 다른사람이 올린 게시글
    var otherBoards = ListLivedata<Board>()
    var otherBoardsCount = MutableLiveData(0)
    fun clearBoards(){
        otherBoards.clear()
    }

    fun getInfo(id: Int) = viewModelScope.launch {
        val response = repo.getInfo(id)
        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200) _memberInfo.value = it.body()
            },
            onError = {

            },
            onFailure = {

            }
        )
    }

    fun getBoardsOther(memberId: Int, number: Int) = viewModelScope.launch {
        val response = repo.getUserBoards(memberId, number)

        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200) {
                    it.body()?.items?.boards?.toMutableList()?.let { boards ->
                        otherBoards.addAll(boards)
                    }
                    otherBoardsCount.value = it.body()?.items?.TotalElements
                }
            },
            onError = {},
            onFailure = {}
        )
    }

}