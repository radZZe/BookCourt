package com.example.bookcourt.data.repositories

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import javax.inject.Inject


class NetworkRepository @Inject constructor(
    private val client: OkHttpClient
) {
    suspend fun getAllBooks(context: Context):String?{
        var json = File(context.filesDir,"book.json").readText()
//        val request: Request = Request.Builder()
//            .url("https://bookcourttest-ee89c-default-rtdb.asia-southeast1.firebasedatabase.app/books.json")
//            .method("GET",null)
//            .addHeader("Content-Type", "application/json")
//            .build()
//        val response = client.newCall(request).execute()
//        return response.body?.string()
        return json
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

    private fun jsonDataFromAssetFile(filename:String,context:Context ):String{
        var json = ""
        json = context.assets
            .open(filename)
            .bufferedReader().use {
                it.readText()
            }

        return json
    }
}