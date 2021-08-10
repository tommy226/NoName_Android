package com.sungbin.noname.upload.repository

import com.sungbin.noname.App
import com.sungbin.noname.network.ServerImpl
import com.sungbin.noname.upload.data.BoardsContentRequest
import com.sungbin.noname.upload.data.BoardsContentResponse
import com.sungbin.noname.util.PreferenceUtil
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class UploadRepository {
    private val token = App.prefs.getString(PreferenceUtil.AccessToken, "")

    fun uploadContent(content: String) =
        ServerImpl.service.boardContentUpload(
            token = token,
            Board = BoardsContentRequest(content = content)
        )     // retrofit api service

    fun uploadFiles(files: MutableList<MultipartBody.Part>, id: String) =
        ServerImpl.service.boardFileUpload(
            token = token,
            files = files,
            boardId = id
        )
}