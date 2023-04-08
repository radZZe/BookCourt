package com.example.bookcourt.models.book

import com.example.bookcourt.models.metrics.DataBookMetric

@kotlinx.serialization.Serializable
data class Book(
    val isbn: String?,
    val bookInfo: BookInfo,
    var onSwipeDirection: String?,
    val shopOwner: String,
    val buyUri: String
){
    fun toBookMetric(swipeType:String): DataBookMetric {
        return DataBookMetric(
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

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            bookInfo.title,
            bookInfo.author,
            bookInfo.genre,
            "${bookInfo.title}${bookInfo.author}",
            "${bookInfo.title.first()}",
            "${bookInfo.title.first()} ${bookInfo.author.first()}",
            "${bookInfo.title} ${bookInfo.author.first()}"
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
