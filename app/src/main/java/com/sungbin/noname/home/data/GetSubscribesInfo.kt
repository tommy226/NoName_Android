package com.sungbin.noname.home.data

data class GetSubscribesInfo(
    val items: InfoItems,
    val message: String
)

data class InfoItems(
    val TotalElements: Int,
    val page: Int,
    val subscribes: List<Subscribe>
)