package com.example.bookcourt.data.room.user

import com.example.bookcourt.data.room.user.sources.UserLocalSource
import com.example.bookcourt.data.room.user.sources.UserNetworkSource
import com.example.bookcourt.models.user.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val networkSource: UserNetworkSource,
    private val localSource: UserLocalSource
) : UserRepositoryI {
    override suspend fun loadData(id: String): User? {
        var result = localSource.getData(id)
        if (result == null) result = networkSource.getData(id)
        // TODO всегда false почему?
        return  result
    }

    override suspend fun saveData(user: User) {
        localSource.saveData(user)
        //networkSource.saveData(user)
    }

    override suspend fun updateData(user: User) {
        localSource.updateUser(user)
        //networkSource.updateUser(user)
    }

}