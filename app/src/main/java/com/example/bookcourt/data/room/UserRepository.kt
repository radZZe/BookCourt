package com.example.bookcourt.data.room

import com.example.bookcourt.models.User

interface UserRepository {

    suspend fun addUser(user: User)

    suspend fun getUserById(id: String): User

    suspend fun updateUser(user: User)

}