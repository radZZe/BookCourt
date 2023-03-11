package com.example.bookcourt.models.metrics

import com.example.bookcourt.models.metrics.DataSessionMetric

@kotlinx.serialization.Serializable
data class SessionMetric(
    val UUID:String,
    val GUID:String,
    val Type:String,
    val Data: DataSessionMetric,
    val Date:String,
)
