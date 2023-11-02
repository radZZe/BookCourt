package com.example.bookcourt.models.search

@kotlinx.serialization.Serializable
data class SearchBook(
    val bookId:String,
    val title:String,
    val imageUrl:String
)
