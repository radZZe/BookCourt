package com.example.bookcourt.data.user

import com.example.bookcourt.models.user.User

interface UserRepository {
    suspend fun loadData(id:String): User?
    suspend fun saveData(user:User)
    suspend fun updateData(user:User)
}