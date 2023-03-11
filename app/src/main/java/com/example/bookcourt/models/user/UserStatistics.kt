package com.example.bookcourt.models.user

import com.example.bookcourt.models.book.Book


data class UserStatistics(
//    val userId: String,
    val readBooksList: List<Book>,
    val wantToRead: List<Book>,
)

//    val numberOfReadBooks:Int,
//    val numberOfLikedBooks:Int,
//    val numberOfDislikedBooks:Int,
//    val favoriteGenreList: List<String>,
