package com.example.bookcourt.models

import kotlinx.serialization.Serializable

@Serializable
data class Statistics(
    val numberOfReadBooks:Int,
    val numberOfLikedBooks:Int,
    val favoriteGenreList:List<String>
)
