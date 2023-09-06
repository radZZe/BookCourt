package com.example.bookcourt.models

import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.book.BookInfo
import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val id: String,
    val data: BookData,
) {
    fun toBook(): Book {
        val bookInfo = BookInfo(
            title = data.name,
            author = data.author,
            description = data.description,
            numberOfPages = data.numberOfPage,
            rate = data.rate,
            genre = data.genre,
            image = data.image,
            price = data.price,
        )
        return Book(
            isbn = this.id,
            bookInfo = bookInfo,
            onSwipeDirection = null,
            shopOwner = data.shop_owner,
            buyUri = data.buy_uri,
        )
    }
}

@Serializable
data class BookData(
    val name: String,
    val author: String,
    val description: String,
    val createdAt: String,
    val numberOfPage: String,
    val rate: Int,
    val owner: String,
    val genre: String,
    val image: String,
    val loadingAt: String,
    val price: Int,
    val shop_owner: String,
    val buy_uri: String
)
