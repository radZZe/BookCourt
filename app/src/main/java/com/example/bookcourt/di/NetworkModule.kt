package com.example.bookcourt.di

import com.example.bookcourt.data.api.BooksApi
import com.example.bookcourt.data.api.MetricsApi
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.utils.ApiUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideNetworkRepository(
        booksApi: BooksApi
    ):NetworkRepository{
        return NetworkRepository(booksApi)
    }

    @Provides
    @Singleton
    fun provideMetricsApi():MetricsApi{
        return Retrofit.Builder()
            .baseUrl(ApiUrl.METRICS_URL)
            .build()
            .create(MetricsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBooksApi():BooksApi{
       return Retrofit.Builder()
            .baseUrl(ApiUrl.BOOKS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }

}