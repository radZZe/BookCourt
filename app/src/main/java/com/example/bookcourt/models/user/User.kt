package com.example.bookcourt.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bookcourt.models.book.Book
import kotlinx.serialization.Serializable

@Entity(tableName = "user_table")
@Serializable
data class User(
    @PrimaryKey(autoGenerate = false) val uid: String,
    val name:String? = null,
    val email:String,
    val city: String,
    val image:String? = null,
    val dayBD:String? = null,
    val sex:String? = null,
    var readBooksList: MutableList<Book>,
    var wantToRead: MutableList<Book>,
//    @Embedded
//    @Contextual
//    var statistics: UserStatistics,
)
//    val image : String,