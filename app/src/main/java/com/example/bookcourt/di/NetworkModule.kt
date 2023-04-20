package com.example.bookcourt.di

import com.example.bookcourt.data.repositories.NetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkRepository():NetworkRepositoryImpl{
        return NetworkRepositoryImpl()
    }
}