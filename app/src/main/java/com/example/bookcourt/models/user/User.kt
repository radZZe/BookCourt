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
    val nickname: String? = null,
    var email:String,
    val surname: String? = null,
    val image:String? = null,
    val dayBD:String? = null,
    val sex:Sex? = null,
    var liked: MutableList<Book>,
    var readBooksList: MutableList<Book>,
    var wantToRead: MutableList<Book>,
){

}

enum class Sex{
    MALE,FEMALE
}