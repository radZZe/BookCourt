package com.example.bookcourt.models

@kotlinx.serialization.Serializable
data class BookMetricRemote(
    val UUID:String,
    val GUID:String,
    val Type:String,
    val Data:BookMetric,
    val Date:String,
)
