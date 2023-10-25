package com.example.bookcourt.data.room.user

import com.example.bookcourt.models.user.User

interface UserRepositoryI {
    suspend fun loadData(id:String): User?
    suspend fun saveData(user:User)
    suspend fun updateData(user:User)
    suspend fun deleteUser(user: User)
}