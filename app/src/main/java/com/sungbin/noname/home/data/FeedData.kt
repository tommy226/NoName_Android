package com.sungbin.noname.home.data

data class FeedData(
    val items: Feed,
    val message: String
)

data class Feed(
    val board: Board
)

data class Board(
    val content: String,
    val fileDtos: List<FileDto>,
    val id: Int?,
    val name: String
)

data class FileDto(
    val boardId: Int,
    val fileType: String,
    val id: Int,
    val originalName: String,
    val path: String,
    val serverName: String,
    val src: String
)