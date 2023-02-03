package com.example.bookcourt.models

import androidx.compose.runtime.MutableState

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
    val onSwipeDirection:MutableState<String?>
)
