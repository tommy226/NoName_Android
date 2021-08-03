package com.sungbin.noname.upload.data

data class BoardsContentResponse(
    val items: Items,
    val message: String
)

data class Board(
    val content: String,
    val id: String
)

data class Items(
    val board: Board
)