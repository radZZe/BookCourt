package com.example.bookcourt.models

import kotlinx.serialization.Serializable

@Serializable
data class Statistics(
    val numberOfReadBooks:Int,
    val numberOfLikedBooks:Int,
    val numberOfDislikedBooks:Int,
    val favoriteGenreList:List<String>,
    val wantToRead: List<String>,
    val userClass:String ="",
)
