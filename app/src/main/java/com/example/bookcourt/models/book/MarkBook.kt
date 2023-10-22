package com.example.bookcourt.models.book

import kotlinx.serialization.Serializable

@Serializable
data class MarkBook(
    val bookId:String,//guid
    val markType:String,
)
