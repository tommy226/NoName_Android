package com.sungbin.noname.home.data

data class SubscribeResponse(
    val items: SubscribeItems,
    val message: String
)

data class SubscribeItems(
    val boardId: Int,
    val fallowAccount: String,
    val fallowId: Int,
    val fallowName: String,
    val id: Int,
    val ownerId: Int
)