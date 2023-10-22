package com.example.bookcourt.models.book

import kotlinx.serialization.Serializable

@Serializable
data class BookRetrofit(
    val bookId:String ,//guid
    val sellerId:String ,//guid
    val title:String,
    val author:String,
    val coverType:String,
    val pages:Int,
    val bookFormat:String,
    val language:String,
    val isbn:String,
    val description:String,
    val imageUrl:String,
    val publisherId:Int,
)
