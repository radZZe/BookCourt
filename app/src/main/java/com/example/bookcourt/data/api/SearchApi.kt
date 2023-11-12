package com.example.bookcourt.data.api

import com.example.bookcourt.models.search.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("book/search")
    suspend fun searchBooks(
        @Query("searchText")
        searchText:String
    ):SearchResponse
}