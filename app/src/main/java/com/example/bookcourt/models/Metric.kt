package com.example.bookcourt.models

import kotlinx.serialization.Serializable

@Serializable
data class Metric(
    val GUID:String,
    val type:String,
    val data:String,
    val date:String,
)
