package com.example.bookcourt.models

import kotlinx.serialization.Serializable

@Serializable
data class Metric(
    val UUID:String,
    val GUID:String,
    val Type:String,
    val Data:String,
    val Date:String,
)
