package com.example.bookcourt.data.repositories

import android.content.Context

interface NetworkRepository {
    suspend fun getAllBooks(context: Context):String?
    suspend fun getUserData(context: Context):String
    fun jsonDataFromAssetFile(filename:String,context:Context ):String
}