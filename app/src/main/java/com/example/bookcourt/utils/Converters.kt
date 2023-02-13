package com.example.bookcourt.utils

import androidx.room.TypeConverter
import com.example.bookcourt.models.Book
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONArray


class Converters {

    @TypeConverter
    fun convertBookListToString(list: List<Book>): String {
        return Json.encodeToString(
            serializer = ListSerializer(Book.serializer()),
            list
        )
    }

    @TypeConverter
    fun convertJsonToBookList(json: String) : List<Book> {
        return Json.decodeFromString<MutableList<Book>>("""$json""")
    }

    @TypeConverter
    fun convertBookToJson(book: Book) : String {
        return Json.encodeToString(serializer = Book.serializer(), book)
    }

    @TypeConverter
    fun convertJsonToBook(json: String) : Book {
        return Json.decodeFromString<Book>("""$json""")
    }

//    fun convertToMap(value: String):MutableMap<String,Int>{
//        return Json.decodeFromString(value)
//    }
//
//    fun convertFromMap(map:Map<String,Int>):String{
//        return   Json.encodeToJsonElement(map).toString()
//    }
//
//    @TypeConverter
//    fun convertToBookList(value:String):ArrayList<Book>{
//        return Json.decodeFromString(value)
//    }
//
//    @TypeConverter
//    fun convertToStringList(value:String):ArrayList<String>{
//        return Json.decodeFromString(value)
//    }
//
//    @TypeConverter
//    fun convertFromBookList(list: List<Book>): String {
//        return   Json.encodeToJsonElement(list.map { it.toString() }).toString()
//    }
//
//    @TypeConverter
//    fun convertFromStringList(list: List<Book>): String {
//        return   Json.encodeToJsonElement(list).toString()
//    }
}