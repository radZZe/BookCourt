package com.example.bookcourt.models.delivery

@kotlinx.serialization.Serializable
data class DeliveryBook(
    val bookId: String,
    val count: Int
)
