package com.example.bookcourt.models

data class User(
    val name:String,
    val surname:String,
    val email:String,
    val image:String,
    var statistics:Statistics,
)
