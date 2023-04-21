package com.example.bookcourt.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bookcourt.models.user.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Query("SELECT * FROM user_table WHERE uid = :id")
    suspend fun getUserById(id: String) : User

    @Update
    suspend fun updateUser(user: User)
}