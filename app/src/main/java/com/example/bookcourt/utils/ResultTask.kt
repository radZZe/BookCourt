package com.example.bookcourt.utils

sealed class ResultTask<T>(
    val message:String? = null
){
    class Success<T>(message: T?):ResultTask<T>()
    class Error<T>(message: String?):ResultTask<T>(message = message)
}