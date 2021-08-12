package com.sungbin.noname.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServerImpl {
    private const val BASE_URL = "http://192.168.1.29:8082/"

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    val headerInterceptor = Interceptor {
        val request = it.request()
            .newBuilder()
            .addHeader("content-type", "application/json")
            .build()
        return@Interceptor it.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor())
        .addInterceptor(headerInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

//    private val client = SSLFactory().unsafeOkHttpClient        // SSL 인증서 에러 시 사용 (http 로컬 통신)
//        .addInterceptor(httpLoggingInterceptor())
//        .addInterceptor(headerInterceptor)
//        .connectTimeout(60, TimeUnit.SECONDS)
//        .readTimeout(60, TimeUnit.SECONDS)
//        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: ServerService = retrofit.create(ServerService::class.java)
}