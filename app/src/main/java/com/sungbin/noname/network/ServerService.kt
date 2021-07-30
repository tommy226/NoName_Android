package com.sungbin.noname.network

import com.sungbin.noname.login.data.LoginRequest
import com.sungbin.noname.login.data.LoginResponse
import com.sungbin.noname.signup.data.AccountCheckRequest
import com.sungbin.noname.signup.data.AccountCheckResponse
import com.sungbin.noname.signup.data.RegisterRequest
import com.sungbin.noname.signup.data.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ServerService {
    @PUT("auth/") // login
    fun login(@Body userinfo: LoginRequest): Call<LoginResponse>

    @POST("auth/authority")
    fun accountDup(@Body account: AccountCheckRequest): Call<AccountCheckResponse>

    @POST("auth/") // sign
    fun register(@Body registerinfo: RegisterRequest): Call<RegisterResponse>

    @Multipart
    @POST("board/") // board upload
    fun boardUpload(
        @Part images: MutableList<MultipartBody.Part>
    )
}