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

//    val name:String,
//    val author:String,
//    val description:String,
//    val createdAt:String,
//    val numberOfPage:String,
//    val rate:Int,
//    val owner:String,
//    val price: Int,
//    val genre:String,
//    val image:String,
