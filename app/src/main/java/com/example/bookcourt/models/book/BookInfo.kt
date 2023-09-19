package com.example.bookcourt.models.book

import androidx.compose.runtime.MutableState
import kotlinx.serialization.Serializable

@Serializable
data class BookInfo(
//    val bookId: String,
    val title: String,
    val author: String,
    val description: String,
    val numberOfPages: String,
    val rate: Float,
    val genre: String,
    val price: Int,
    val image:String,
)
