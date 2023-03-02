package com.example.bookcourt.models

@kotlinx.serialization.Serializable
data class ClickMetricRemote(
    val UUID:String,
    val GUID:String,
    val Type:String,
    val Data:ClickMetric,
    val Date:String,
)
