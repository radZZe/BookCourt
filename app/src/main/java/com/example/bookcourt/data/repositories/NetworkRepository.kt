package com.example.bookcourt.data.repositories
import android.content.Context
import com.example.bookcourt.data.api.BooksApi
import com.example.bookcourt.models.BookRemote
import com.example.bookcourt.utils.ResultTask
import javax.inject.Inject


class NetworkRepository @Inject constructor(
    private val booksApi: BooksApi
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

    suspend fun getAllBooksRemote():ResultTask<List<BookRemote>>{
       val response =  try {
            ResultTask.Success(data = booksApi.fetchBooks())
        }catch (e:java.lang.Exception){
            ResultTask.Error(message = e.message)
        }
        return response
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