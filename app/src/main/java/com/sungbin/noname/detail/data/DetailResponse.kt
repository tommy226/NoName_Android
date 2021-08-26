package com.sungbin.noname.detail.data

import com.sungbin.noname.home.data.Board

data class DetailResponse(
    val items: Items,
    val message: String
)

data class Items(
    val board: Board
)