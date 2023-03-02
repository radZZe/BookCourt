package com.example.bookcourt.models

@kotlinx.serialization.Serializable
data class Book(
    val isbn: String?,
    val bookInfo: BookInfo,
    var onSwipeDirection: String?,
    val shopOwner: String,
    val buyUri: String
){
    fun toBookMetric(swipeType:String):BookMetric{

        return BookMetric(
            isbn = this.isbn,
            description = this.bookInfo.description,
            swipeType = swipeType,
            title = this.bookInfo.title,
            author = this.bookInfo.author,
            numberOfPages = this.bookInfo.numberOfPages,
            genre = this.bookInfo.genre,
            price = this.bookInfo.price.toString(),
            shopOwner = this.shopOwner
        )
    }
}
