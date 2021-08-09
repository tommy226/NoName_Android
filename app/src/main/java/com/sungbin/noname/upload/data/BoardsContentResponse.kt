package com.sungbin.noname.upload.data

data class BoardsContentResponse(
    val items: BoardItems,
    val message: String
)

data class Board(
    val content: String,
    val id: String
)

data class BoardItems(
    val board: Board
)