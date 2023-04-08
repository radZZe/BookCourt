package com.example.bookcourt.data.user.sources

import com.example.bookcourt.models.user.User

interface UserSourceI {
    suspend fun getData(id:String): User
    suspend fun saveData(user:User)
    suspend fun updateUser(user:User)
}