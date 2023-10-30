package com.example.bookcourt.models.library

import kotlinx.serialization.Serializable
@Serializable
data class InfoBlock (
    val title:String,
    val description:String,
    val logoUrl: String
)