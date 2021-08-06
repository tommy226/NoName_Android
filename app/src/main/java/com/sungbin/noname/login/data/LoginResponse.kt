package com.sungbin.noname.login.data

data class LoginResponse(
    val items: Items
)
data class Items(
    val account: String,
    val name: String
)