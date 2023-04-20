package com.example.bookcourt.di

import android.app.Application
import androidx.room.Room

import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.repositories.MetricsRepositoryImpl
import com.example.bookcourt.data.room.searchRequest.SearchRequestDatabase
import com.example.bookcourt.data.room.searchRequest.SearchRequestRepository
import com.example.bookcourt.data.user.UserDatabase
import com.example.bookcourt.data.user.sources.UserLocalSourceImpl
import com.example.bookcourt.data.user.sources.UserNetworkSourceImpl
import com.example.bookcourt.data.user.sources.UserSource
import com.example.bookcourt.data.user.UserRepositoryImpl
import com.example.bookcourt.data.user.UserRepository
import com.example.bookcourt.utils.Converters
import com.example.bookcourt.utils.Hashing
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Named
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
    fun provideJson(): Json {
        return Json
    }

    @Provides
    @Singleton
    fun provideMetrics(dataStoreRepository: DataStoreRepository,json:Json): MetricsRepository {
        return MetricsRepositoryImpl(dataStoreRepository,json)
    }

    @Provides
    fun provideHashing():Hashing{
        return Hashing
    }

    @Provides
    @Singleton
    fun provideUserDatabase(application: Application,json:Json) : UserDatabase {
        val converter = Converters(json)
        return Room.databaseBuilder(
            application,
            UserDatabase::class.java,
            "user_database"
        ).addTypeConverter(converter)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(@Named("UserNetworkSource") networkSource: UserSource, @Named("UserLocalSource") localSource: UserSource) : UserRepository {
        return UserRepositoryImpl(networkSource,localSource)
    }

    @Provides
    @Singleton
    @Named("UserNetworkSource")
    fun provideUserNetworkSource() : UserSource {
        return UserNetworkSourceImpl()
    }

    @Provides
    @Singleton
    @Named("UserLocalSource")
    fun provideUserLocalSource(db: UserDatabase) : UserSource {
        return UserLocalSourceImpl(db.userDao())
    }

    @Provides
    @Singleton
    fun provideSearchRequestDatabase(application: Application) : SearchRequestDatabase {
        return Room.databaseBuilder(
            application,
            SearchRequestDatabase::class.java,
            "search_request_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideSearchRequestRepository(db: SearchRequestDatabase) : SearchRequestRepository {
        return SearchRequestRepository(db.searchRequestDao())
    }
}
