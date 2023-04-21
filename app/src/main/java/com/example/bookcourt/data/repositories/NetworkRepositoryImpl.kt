package com.example.bookcourt.data.repositories

import android.content.Context
import okhttp3.*
import javax.inject.Inject


class NetworkRepositoryImpl:NetworkRepository {
    // TODO Сделать два источника данных кэш и сеть , для них сделать репозитории
    override suspend fun getAllBooks(context: Context):String{
        val json = context.assets.open("books.json").bufferedReader().use {
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

    override suspend fun getUserData(context: Context):String{
//        val request: Request = Request.Builder()
//            .url("https://bookcourttest-ee89c-default-rtdb.asia-southeast1.firebasedatabase.app/users/${userId}.json")
//            .method("GET", null)
//            .addHeader("Content-Type", "application/json")
//            .build()
//        val response = client.newCall(request).execute()
//        return response.body?.string()
        val json = context.assets.open("user.json").bufferedReader().use {
            it.readText()
        }
        return json
    }

     override fun jsonDataFromAssetFile(filename:String, context:Context ):String{
        val json = context.assets
            .open(filename)
            .bufferedReader().use {
                it.readText()
            }

        return json
    }
}