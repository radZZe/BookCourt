package com.example.bookcourt.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.bookcourt.models.book.Book
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ProvidedTypeConverter
class Converters @Inject constructor(
    private val json: Json
) {

    @TypeConverter
    fun convertBookListToString(list: List<Book>): String {
        return json.encodeToString(
            serializer = ListSerializer(Book.serializer()),
            list
        )
    }

    @TypeConverter
    fun convertJsonToBookList(jsonText: String) : List<Book> {

        return json.decodeFromString<MutableList<Book>>(jsonText)
    }

    @TypeConverter
    fun convertBookToJson(book: Book) : String {
        return json.encodeToString(serializer = Book.serializer(), book)
    }

    @TypeConverter
    fun convertJsonToBook(jsonText: String) : Book {
        return json.decodeFromString<Book>(jsonText)
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