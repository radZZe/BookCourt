package com.example.bookcourt.data.repositories

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.nio.charset.Charset
import javax.inject.Inject


class NetworkRepository @Inject constructor(
    private val client: OkHttpClient
) {
    // TODO Сделать два источника данных кэш и сеть , для них сделать репозитории
    suspend fun getAllBooks(context: Context):String?{
        var json = context.assets.open("books.json").bufferedReader().use {
            it.readText()
        }
//        val request: Request = Request.Builder()
//            .url("https://bookcourttest-ee89c-default-rtdb.asia-southeast1.firebasedatabase.app/books.json")
//            .method("GET",null)
//            .addHeader("Content-Type", "application/json")
//            .build()
//        val response = client.newCall(request).execute()
//        return response.body?.string()
        return json
    }

    suspend fun getUserData(context: Context):String{
//        val request: Request = Request.Builder()
//            .url("https://bookcourttest-ee89c-default-rtdb.asia-southeast1.firebasedatabase.app/users/${userId}.json")
//            .method("GET", null)
//            .addHeader("Content-Type", "application/json")
//            .build()
//        val response = client.newCall(request).execute()
//        return response.body?.string()
        var json = context.assets.open("user.json").bufferedReader().use {
            it.readText()
        }
        return json
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