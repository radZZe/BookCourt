package com.example.bookcourt.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bookcourt.models.book.Book
import kotlinx.serialization.Serializable

@Entity(tableName = "user_table")
@Serializable
data class User(
    @PrimaryKey(autoGenerate = false) val uid: String,
    val name:String,
    val surname:String,
    val phone: String,
    val city: String,
    var readBooksList: MutableList<Book>,
    var wantToRead: MutableList<Book>,
//    @Embedded
//    @Contextual
//    var statistics: UserStatistics,
)
//    val image : String,