package com.example.bookcourt.models

import kotlinx.serialization.Serializable

@Serializable
data class SessionTime(
    val SessionTime: String,
    val location :String,
    val appversion:String
)
