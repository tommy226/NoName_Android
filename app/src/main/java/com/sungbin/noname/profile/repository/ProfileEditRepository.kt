package com.sungbin.noname.profile.repository

import com.sungbin.noname.App
import com.sungbin.noname.network.ServerImpl
import com.sungbin.noname.profile.data.ProfileEditRequest
import com.sungbin.noname.profile.data.ProfileImageResponse
import com.sungbin.noname.util.PreferenceUtil
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class ProfileEditRepository {
    private val token = App.prefs.getString(PreferenceUtil.AccessToken, "")
    private val account = App.prefs.getString(PreferenceUtil.Account, "").split("@")[0]

    fun profileEdit(name: String, info: String) =
        ServerImpl.service.profileEdit(
            token = token,
            account = account,
            ProfileEditRequest(name = name, info = info)
        )

    fun profileImageEdit(
        image: MultipartBody.Part,
//        data: HashMap<String, RequestBody>
    ): Call<ProfileImageResponse>? = ServerImpl.service.profileImageUpload(
        token = token,
        image = image,
        account = account
    )
}