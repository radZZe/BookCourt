package com.example.bookcourt.data.room.searchRequest

import com.example.bookcourt.models.user.SearchRequest

interface SearchRequestRepositoryI {
    suspend fun addRequest(request: SearchRequest)
    suspend fun getRequests() : List<SearchRequest>
}