package com.example.bookcourt.data.repositories

import com.example.bookcourt.models.user.User

interface UserRepository {

    suspend fun addUser(user: User)

    suspend fun getUserById(id: String): User

    suspend fun updateUser(user: User)

}