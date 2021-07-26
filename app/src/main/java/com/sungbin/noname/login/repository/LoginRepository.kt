package com.sungbin.noname.login.repository

import com.sungbin.noname.login.data.LoginRequest
import com.sungbin.noname.network.ServerImpl

class LoginRepository {
    fun login(account: String, password: String) = ServerImpl.service.login(LoginRequest(account, password))
}