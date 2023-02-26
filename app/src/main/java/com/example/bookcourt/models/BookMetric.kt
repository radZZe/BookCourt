package com.example.bookcourt.models

@kotlinx.serialization.Serializable
data class BookMetric(
    val bookId: String?,
    val swipeType:String,
    val title: String,
    val author: String,
    val numberOfPages: String,
    val rate: Int,
    val genre: String,
    val price: Int,
    val shopOwner: String,
)
