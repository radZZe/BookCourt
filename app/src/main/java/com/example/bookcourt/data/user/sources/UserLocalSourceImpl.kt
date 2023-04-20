package com.example.bookcourt.data.user.sources

import com.example.bookcourt.data.user.UserDao
import com.example.bookcourt.models.user.User

class UserLocalSourceImpl(
    private val dao: UserDao
): UserSource {
    override suspend fun getData(id: String): User {
        return dao.getUserById(id)
    }

    override suspend fun saveData(user: User) {
        dao.addUser(user)
    }

    override suspend fun updateUser(user: User) {
        dao.updateUser(user)
    }
}