package com.example.bookcourt.data.user.searchRequest

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookcourt.models.user.SearchRequest

@Dao
interface SearchRequestDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRequest(request: SearchRequest)

    @Query("SELECT * FROM search_request_table")
    suspend fun getRequests() : List<SearchRequest>
}