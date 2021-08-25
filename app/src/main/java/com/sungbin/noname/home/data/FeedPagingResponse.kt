package com.sungbin.noname.home.data

data class FeedPagingResponse(
    val items: FeedItems,
    val message: String
)

data class FeedItems(
    val TotalElements: Int,
    val boards: List<Board>,
    val page: Int
)

data class Board(
    val content: String,
    val fileDtos: List<FileDto>,
    val id: Int?,
    val memberDto: MemberDto,
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

data class MemberDto(
    val id: Int,
    val info: String,
    val name: String,
    val src: String
)