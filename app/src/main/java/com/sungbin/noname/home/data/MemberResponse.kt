package com.sungbin.noname.home.data

data class MemberResponse(
    val items: Items,
    val message: String
)

data class Items(
    val file: File,
    val member: Member,
    val src: String
)

data class Member(
    val account: String,
    val info: String,
    val name: String,
    val fallow: Fallow? = null
)

data class File(
    val fileType: String,
    val id: Int,
    val originalName: String,
    val serverName: String,
    val path: String,
    val boardId: Int
)