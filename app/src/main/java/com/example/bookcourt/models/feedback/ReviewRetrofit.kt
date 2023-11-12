package com.example.bookcourt.models.feedback

import kotlinx.serialization.Serializable

@Serializable
data class ReviewRetrofit(
    val userId:String,
    val nickname:String?,
    val score:Int,
    val date:String,
    val text:String,
)
