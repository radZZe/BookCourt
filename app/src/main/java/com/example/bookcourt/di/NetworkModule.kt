package com.example.bookcourt.di

import com.example.bookcourt.data.repositories.NetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient




@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .build()
    }
    @Provides
    fun provideNetworkRepository(client: OkHttpClient):NetworkRepository{
        return NetworkRepository(client)
    }
}