package com.example.bookcourt.utils

sealed class ResultTask<T>(
    val data:T? = null,
    val message:String? = null
){
    class Success<T>(data: T?):ResultTask<T>(data)
    class Error<T>(message: String?):ResultTask<T>(message = message)
}