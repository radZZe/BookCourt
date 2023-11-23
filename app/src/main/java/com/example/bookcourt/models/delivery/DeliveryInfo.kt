package com.example.bookcourt.models.delivery

@kotlinx.serialization.Serializable
data class DeliveryInfo (
    val regionName: String,
    val cityName: String,
    val address: String,
    val index: String,
    val books: List<DeliveryBook>
)