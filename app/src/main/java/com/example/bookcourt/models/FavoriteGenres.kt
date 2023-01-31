package com.example.bookcourt.models

@kotlinx.serialization.Serializable
data class FavoriteGenres(
    var firstPlace:String,
    var secondPlace:String,
    var thirdPlace:String
    )
