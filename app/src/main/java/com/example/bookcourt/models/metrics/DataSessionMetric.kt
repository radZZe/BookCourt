package com.example.bookcourt.models.metrics

import kotlinx.serialization.Serializable

@Serializable
data class DataSessionMetric(
    val SessionTime: String,
    val location :String,
    val appversion:String
)
