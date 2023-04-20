package com.example.bookcourt.data.user

import com.example.bookcourt.data.user.sources.UserSource
import com.example.bookcourt.models.user.User
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    @Named("UserNetworkSource") private val networkSource: UserSource,
    @Named("UserLocalSource") private val localSource: UserSource
) : UserRepository {
    override suspend fun loadData(id: String): User {
        return localSource.getData(id)
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