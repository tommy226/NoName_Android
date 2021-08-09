package com.sungbin.noname.profile.data

data class ProfileEditResponse(
    val items: ProfileEditItems
)

data class ProfileEditItems(
    val id: String,
    val account: String,
    val info: String,
    val name: String
)