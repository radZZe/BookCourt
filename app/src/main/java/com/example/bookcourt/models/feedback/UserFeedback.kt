package com.example.bookcourt.models.feedback

import kotlinx.serialization.Serializable

@Serializable
data class UserFeedback(
    var username:String,
    //var userImage:String,
    var description:String,
    var rate:Int,
    var date:String,
)
