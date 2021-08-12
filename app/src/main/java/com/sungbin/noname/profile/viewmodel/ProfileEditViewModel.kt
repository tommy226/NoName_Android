package com.sungbin.noname.profile.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sungbin.noname.home.viewmodel.SharedViewModel
import com.sungbin.noname.profile.data.ProfileEditResponse
import com.sungbin.noname.profile.data.ProfileImageResponse
import com.sungbin.noname.profile.repository.ProfileEditRepository
import com.sungbin.noname.util.Event
import com.sungbin.noname.util.customEnqueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileEditViewModel : SharedViewModel() {
    private val TAG = ProfileEditViewModel::class.java.simpleName
    private val repo = ProfileEditRepository()

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    val inputName = MutableLiveData<String>("")
    val inputInfo = MutableLiveData<String>("")

    var selectedImage: Uri? = null       // 프로필 사진 편집| 사용자는 프로필 사진을 변경 하지 않을 수도 있다

    private val _toast = MutableLiveData<Event<String>>()
    val toast: LiveData<Event<String>>
        get() = _toast


    private val _idResponse = MutableLiveData<ProfileEditResponse>()
    val idResponse: LiveData<ProfileEditResponse>
        get() = _idResponse

    private val _profileResult = MutableLiveData<ProfileImageResponse>()
    val profileResult: LiveData<ProfileImageResponse>
        get() = _profileResult

    fun editRequest(name: String, info: String) = viewModelScope.launch {
        val response = repo.profileEdit(name, info)
        response.customEnqueue(
            onSuccess = {
                if (it.code() == 200) {
                    _idResponse.value = it.body()
                    _toast.value = Event("프로필 편집 완료")
                }
            },
            onError = {
                _toast.value = Event("서버에 문제가 있습니다. 다시 시도해주세요")
            },
            onFailure = {
                _toast.value = Event("서버에 문제가 있습니다. 다시 시도해주세요")
            }
        )
    }

    fun editImageRequest(
        image: MultipartBody.Part,
    ) = viewModelScope.launch {
        val response = repo.profileImageEdit(image)

        response?.customEnqueue(
            onSuccess = {
                if (it.code() == 201) _profileResult.value = it.body()

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