package com.example.bookcourt.models

import kotlinx.serialization.Serializable

@Serializable
data class UserRemote(
    val name:String,
    val surname:String,
    val email:String,
    val image:String,
    val createdAt:String,
    val statistics:Statistics,
    val password:String,
    val token:String,
){
    fun toUser():User{
        return User(name,surname,email,image,statistics)
    }
}
