package com.example.bookcourt.models

import androidx.compose.runtime.MutableState
import kotlinx.serialization.Serializable

@Serializable
data class BookInfo(
//    val bookId: String,
    val title: String,
    val author: String,
    val description: String,
    val numberOfPages: String,
    val rate: Int,
    val genre: String,
    val price: Int,
    val image:String,
)
