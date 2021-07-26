package com.sungbin.noname.network

import com.sungbin.noname.login.data.LoginRequest
import com.sungbin.noname.login.data.LoginResponse
import com.sungbin.noname.signup.data.AccountCheckRequest
import com.sungbin.noname.signup.data.AccountCheckResponse
import com.sungbin.noname.signup.data.RegisterRequest
import com.sungbin.noname.signup.data.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ServerService {
    @PUT("auth/") // login
    fun login(@Body userinfo: LoginRequest): Call<LoginResponse>

    @POST("auth/authority")
    fun accountDup(@Body account: AccountCheckRequest): Call<AccountCheckResponse>

    @POST("auth/") // sign
    fun register(@Body registerinfo: RegisterRequest): Call<RegisterResponse>
}