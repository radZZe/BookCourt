package com.example.bookcourt.models.book

import kotlinx.serialization.Serializable

@Serializable
data class BookRetrofit(
    val id:String ,//guid
    val title:String ,//guid
    val author:String,
    val age:Int,
    val coverType:String,
    val pages:Int,
    val bookFormat:String,
    val language:String,
    val isbn:String,
    val description:String,
    val imageUrl:String,
    val publisherName:String,
    val publisherUrl:String,
    val countLikes:Int,
    val countWantedRead:Int,
    val countDisliked:Int,
    val score:Int,
    val count:Int,
    val price:Int,
    val sellerId:String
)
