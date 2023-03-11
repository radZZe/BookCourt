package com.example.bookcourt.models.metrics

@kotlinx.serialization.Serializable
data class BookMetric(
    val UUID:String,
    val GUID:String,
    val Type:String,
    val Data: DataBookMetric,
    val Date:String,
)
