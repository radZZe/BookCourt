package com.example.bookcourt.models.feedback

import kotlinx.serialization.Serializable

@Serializable
data class ReviewRetrofit(
    val commentId:String,//guid
    val userId:String,//guid
    val bookId:String,//guid
    val commentText:String,
    val commentDate:Int,//timestamp
    val reviewScore:Int,
)
