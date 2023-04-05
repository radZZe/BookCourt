package com.example.bookcourt.di

import android.app.Application
import androidx.room.Room

import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.user.UserRepository
import com.example.bookcourt.data.user.UserRepositoryI
import com.example.bookcourt.data.user.room.UserDatabase
import com.example.bookcourt.data.user.sources.UserLocalSource
import com.example.bookcourt.data.user.sources.UserNetworkSource
import com.example.bookcourt.utils.Hashing
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideDataStore(application: Application): DataStoreRepository {
        return DataStoreRepository(application)
    }



    @Provides
    @Singleton
    fun provideMetrics(dataStoreRepository: DataStoreRepository):MetricsRepository{
        return MetricsRepository(dataStoreRepository)
    }

    @Provides
    fun provideHashing():Hashing{
        return Hashing
    }

    @Provides
    @Singleton
    fun provideUserDatabase(application: Application) : UserDatabase {
        return Room.databaseBuilder(
            application,
            UserDatabase::class.java,
            "user_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(networkSource: UserNetworkSource,localSource: UserLocalSource) : UserRepositoryI {
        return UserRepository(networkSource,localSource)
    }

    @Provides
    @Singleton
    fun provideUserNetworkSource(db: UserDatabase) : UserNetworkSource {
        return UserNetworkSource()
    }

    @Provides
    @Singleton
    fun provideUserLocalSource(db: UserDatabase) : UserLocalSource {
        return UserLocalSource(db.userDao())
    }
}
