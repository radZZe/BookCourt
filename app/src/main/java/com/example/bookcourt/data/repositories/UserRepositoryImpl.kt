package com.example.bookcourt.data.repositories

import com.example.bookcourt.data.room.UserDao
import com.example.bookcourt.data.room.UserRepository
import com.example.bookcourt.models.user.User

class UserRepositoryImpl(
    private val dao: UserDao
): UserRepository {

    override suspend fun addUser(user: User) {
        dao.addUser(user)
    }

    override suspend fun getUserById(id: String): User {
        return dao.getUserById(id)
    }

    override suspend fun updateUser(user: User) {
        dao.updateUser(user)
    }
}