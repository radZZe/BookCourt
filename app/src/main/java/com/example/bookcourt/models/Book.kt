package com.example.bookcourt.models
import androidx.compose.runtime.MutableState
import kotlinx.serialization.Contextual

@kotlinx.serialization.Serializable
data class Book(
    val bookId: String?,
    val bookInfo: BookInfo,
    var onSwipeDirection: String?,
    val shopOwner: String,
    val buyUri: String
    )
