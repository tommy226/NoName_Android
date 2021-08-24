package com.sungbin.noname.home.data

data class GetProfileImageResponse(
    val items: ImageItems,
    val message: String
)

data class ImageItems(
    val file: ImageFile,
    val src: String
)

data class ImageFile(
    val boardId: Int,
    val fileType: String,
    val id: Int,
    val originalName: String,
    val path: String,
    val serverName: String
)