package com.example.bookcourt.data.user.sources

import com.example.bookcourt.models.user.User

class UserNetworkSourceImpl: UserSource {
    override suspend fun getData(id: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun saveData(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User) {
        TODO("Not yet implemented")
    }
}