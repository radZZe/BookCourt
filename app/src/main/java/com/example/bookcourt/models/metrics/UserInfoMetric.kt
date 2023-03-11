package com.example.bookcourt.models.metrics

import com.example.bookcourt.models.metrics.DataUserInfoMetric

@kotlinx.serialization.Serializable
data class UserInfoMetric(
    val UUID:String,
    val GUID:String,
    val Type:String,
    val Data: DataUserInfoMetric,
    val Date:String,
)
