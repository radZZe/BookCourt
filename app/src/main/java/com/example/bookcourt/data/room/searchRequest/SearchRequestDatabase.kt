package com.example.bookcourt.data.room.searchRequest

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookcourt.models.user.SearchRequest


@Database(entities = [SearchRequest::class], version = 1, exportSchema = false)
abstract class SearchRequestDatabase: RoomDatabase() {

    abstract fun searchRequestDao(): SearchRequestDao

}