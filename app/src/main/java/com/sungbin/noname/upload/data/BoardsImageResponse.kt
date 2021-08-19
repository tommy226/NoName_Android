package com.sungbin.noname.upload.data

data class BoardsImageResponse(
    val items: BoardsImageItems,
    val message: String
)

data class BoardsImageItems(
    val Total_elements: Int,
    val files: List<File>
)

data class File(
    val boardId: Int,
    val fileType: String,
    val id: Int,
    val originalName: String,
    val path: String,
    val serverName: String
)