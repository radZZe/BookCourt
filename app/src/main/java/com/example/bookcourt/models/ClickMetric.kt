package com.example.bookcourt.models


@kotlinx.serialization.Serializable
data class ClickMetric(
    val button: String,
    val screen: String
)