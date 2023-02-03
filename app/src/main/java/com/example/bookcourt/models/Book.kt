package com.example.bookcourt.models

@kotlinx.serialization.Serializable
data class Book(
    val name:String,
    val author:String,
    val description:String,
    val createdAt:String,
    val numberOfPage:String,
    val rate:Int,
    val owner:String,
    val genre:String,
    val image:String,
)
