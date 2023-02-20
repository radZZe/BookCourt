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
){
    fun toBookMetric():BookMetric{
        return BookMetric(
            bookId = this.bookId,
            title = this.bookInfo.title,
            author = this.bookInfo.author,
            numberOfPages = this.bookInfo.numberOfPages,
            rate = this.bookInfo.rate,
            genre = this.bookInfo.genre,
            price = this.bookInfo.price,
            shopOwner = this.shopOwner
        )
    }
}
