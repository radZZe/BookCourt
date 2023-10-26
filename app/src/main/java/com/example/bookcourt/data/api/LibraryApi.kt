package com.example.bookcourt.data.api

import com.example.bookcourt.models.library.CatalogResponse
import retrofit2.http.GET

interface LibraryApi {
    @GET("53LD")
    suspend fun fetchCatalog():CatalogResponse
}