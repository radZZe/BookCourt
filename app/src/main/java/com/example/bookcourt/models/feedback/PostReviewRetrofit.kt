package com.example.bookcourt.models.feedback

import kotlinx.serialization.Serializable

@Serializable
data class PostReviewRetrofit(
    val bookId:String,//guid
    val score:Int,
    val comment:String,
)
