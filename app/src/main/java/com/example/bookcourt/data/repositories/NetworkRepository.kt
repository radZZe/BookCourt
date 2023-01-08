package com.example.bookcourt.data.repositories

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import javax.inject.Inject


class NetworkRepository @Inject constructor(
    private val client: OkHttpClient
) {
    suspend fun getAllBooks():String?{
        val request: Request = Request.Builder()
            .url("https://bookcourttest-ee89c-default-rtdb.asia-southeast1.firebasedatabase.app/books.json")
            .method("GET",null)
            .addHeader("Content-Type", "application/json")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }

    suspend fun getUserData(userId:String):String?{
        val request: Request = Request.Builder()
            .url("https://bookcourttest-ee89c-default-rtdb.asia-southeast1.firebasedatabase.app/users/${userId}.json")
            .method("GET", null)
            .addHeader("Content-Type", "application/json")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()
    }
}