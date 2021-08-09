package com.sungbin.noname.profile.data

data class ProfileImageResponse(
    val items: ProfileImageItems,
    val message: String
)

data class ProfileImageItems(
    val file: File
)

data class File(
    val account: String,
    val fileType: String,
    val id: Int,
    val originalName: String,
    val serverName: String
)