package com.example.bookcourt.di

import android.app.Application
import com.example.bookcourt.data.repositories.DataStoreRepository
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

}