package com.example.bookcourt.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDataMetric(
    val name:String,
    val surname:String,
    val phone:String
)

