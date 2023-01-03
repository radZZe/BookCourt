package com.example.bookcourt.models

data class BookRemote(
    val name:String,
    val author:String,
    val description:String,
    val createdAt:String,
    val numberOfPage:String,
    val rate:Int,
    val owner:String,
    val genre:String,
    val image:String,
    val loadingAt:String,
){
    fun toBook():Book{
        return Book(name,author,description,createdAt,numberOfPage,rate,owner,genre,image)
    }
}
