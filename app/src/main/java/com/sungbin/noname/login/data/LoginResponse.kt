package com.sungbin.noname.login.data

data class LoginResponse(
    val items: Items
)
data class Items(
    val id: Int,
    val account: String,
    val info: String,
    val name: String
)