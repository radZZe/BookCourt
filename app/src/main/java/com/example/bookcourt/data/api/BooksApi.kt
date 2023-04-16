package com.example.bookcourt.data.api

import com.example.bookcourt.models.BookDto
import retrofit2.http.GET

interface BooksApi {
    @GET("4N4X")
    suspend fun fetchBooks():List<BookDto>
}