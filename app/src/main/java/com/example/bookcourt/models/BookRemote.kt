package com.example.bookcourt.models

import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable

@Serializable
data class BookRemote(
    val id: String,
    val data: BookData,
) {
    fun toBook(): Book {
        return Book(
            data.name,
            data.author,
            data.description,
            data.createdAt,
            data.numberOfPage,
            data.rate,
            data.owner,
            data.genre,
            data.image,
            mutableStateOf(null),
            price = data.price,
            shop_owner = data.shop_owner,
            buy_uri = data.buy_uri,
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
