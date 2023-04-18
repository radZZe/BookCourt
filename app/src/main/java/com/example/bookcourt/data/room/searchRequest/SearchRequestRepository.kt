package com.example.bookcourt.data.room.searchRequest

import com.example.bookcourt.models.user.SearchRequest

class SearchRequestRepository(
    private val dao: SearchRequestDao
): SearchRequestRepositoryI {

    override suspend fun addRequest(request: SearchRequest) {
        return dao.addRequest(request)
    }

    override suspend fun getRequests(): List<SearchRequest> {
        return dao.getRequests()
    }
}