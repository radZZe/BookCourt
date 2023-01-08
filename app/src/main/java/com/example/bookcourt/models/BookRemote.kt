package com.example.bookcourt.models

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
            data.image
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
)
