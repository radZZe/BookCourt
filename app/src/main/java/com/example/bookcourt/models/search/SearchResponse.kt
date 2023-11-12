package com.example.bookcourt.models.search

import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.book.BookRetrofit

@kotlinx.serialization.Serializable
data class SearchResponse(
    val books:List<SearchBook>,
    val otherBooks:List<SearchBook>
)
