package com.example.bookcourt.data.room.user.sources

import com.example.bookcourt.data.room.user.UserDao
import com.example.bookcourt.models.user.User

class UserLocalSource(
    private val dao: UserDao
): UserSourceI {

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