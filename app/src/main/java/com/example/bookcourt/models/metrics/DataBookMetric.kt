package com.example.bookcourt.models.metrics

@kotlinx.serialization.Serializable
data class DataBookMetric(
    val isbn: String?,
    val description:String,
    val swipeType:String,
    val title: String,
    val author: String,
    val numberOfPages: String,
    val genre: String,
    val price: String,
    val shopOwner: String,
)
