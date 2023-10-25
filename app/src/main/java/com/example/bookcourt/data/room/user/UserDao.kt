package com.example.bookcourt.data.room.user

import androidx.room.*
import com.example.bookcourt.models.user.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Query("SELECT * FROM user_table WHERE uid = :id")
    suspend fun getUserById(id: String) : User

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}