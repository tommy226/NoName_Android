package com.sungbin.noname.signup.repository

import com.sungbin.noname.network.ServerImpl
import com.sungbin.noname.signup.data.AccountCheckRequest
import com.sungbin.noname.signup.data.RegisterRequest

class SignUpRepository {
    fun accountDupCheck(account: String) = ServerImpl.service.accountDup(AccountCheckRequest(account))

    fun register(account: String, password: String, name: String) = ServerImpl.service.register(RegisterRequest(account, password, name))
}