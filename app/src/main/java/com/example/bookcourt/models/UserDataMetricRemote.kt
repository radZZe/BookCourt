package com.example.bookcourt.models

@kotlinx.serialization.Serializable
data class UserDataMetricRemote(
    val UUID:String,
    val GUID:String,
    val Type:String,
    val Data:UserDataMetric,
    val Date:String,
)
