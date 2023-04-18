package com.example.bookcourt.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_request_table")
data class SearchRequest(
    @PrimaryKey(autoGenerate = false) val request: String
)
