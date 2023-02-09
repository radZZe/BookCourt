package com.example.bookcourt.models

import androidx.compose.runtime.MutableState

data class BookInfo(
    val name:String,
    val author:String,
    val description:String,
    val numberOfPage:String,
    val rate:Int,
    val genre:String,
    val price: Int,
)
