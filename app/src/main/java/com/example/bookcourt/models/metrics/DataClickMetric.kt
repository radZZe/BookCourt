package com.example.bookcourt.models.metrics


@kotlinx.serialization.Serializable
data class DataClickMetric(
    val button: String,
    val screen: String
)