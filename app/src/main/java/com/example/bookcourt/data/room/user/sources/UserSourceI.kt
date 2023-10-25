package com.example.bookcourt.data.room.user.sources

import com.example.bookcourt.models.user.User

interface UserSourceI {
    suspend fun getData(id:String): User
    suspend fun saveData(user:User)
    suspend fun updateUser(user:User)
    suspend fun deleteUser(user: User)
}