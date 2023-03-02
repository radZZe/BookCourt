package com.example.bookcourt.models

@kotlinx.serialization.Serializable
data class SessionTimeMetricRemote(
    val UUID:String,
    val GUID:String,
    val Type:String,
    val Data:SessionTime,
    val Date:String,
)
