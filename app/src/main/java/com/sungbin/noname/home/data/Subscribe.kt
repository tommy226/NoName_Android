package com.sungbin.noname.home.data

data class Subscribe(
    val boardId: Int,
    val fallowId: Int,
    val fallowName: String,
    val fallowSrc: String,
    val id: Int,
    val ownerId: Int
)