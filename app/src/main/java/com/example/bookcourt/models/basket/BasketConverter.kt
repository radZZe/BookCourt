package com.example.bookcourt.models.basket

import androidx.room.TypeConverter
import com.example.bookcourt.models.BookData
import com.example.bookcourt.models.BookDto
import com.example.bookcourt.models.book.Book
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BasketConverter {
    val json = Json { encodeDefaults = true }

    @TypeConverter
    fun bookToString(book: Book):String{
        val item = book.isbn?.let {
            BookDto(
                id = it,
                data = BookData(
                     name=book.bookInfo.title,
                     author=book.bookInfo.author,
                     description=book.bookInfo.description,
                     createdAt="13213",
                     numberOfPage=book.bookInfo.numberOfPages,
                     rate=book.bookInfo.rate,
                     owner=book.shopOwner,
                     genre=book.bookInfo.genre,
                     image=book.bookInfo.image,
                     loadingAt="123",
                     price=book.bookInfo.price,
                     shop_owner=book.shopOwner,
                     buy_uri=book.buyUri,
                )
            )
        }
        return json.encodeToString(item)
    }

    @TypeConverter
    fun stringToBook(string: String):Book{
        return Json.decodeFromString<BookDto>(string).toBook()
    }
}