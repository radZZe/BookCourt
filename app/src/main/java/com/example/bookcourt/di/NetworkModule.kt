package com.example.bookcourt.di

import com.example.bookcourt.data.api.BooksApi
import com.example.bookcourt.data.api.MetricsApi
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.utils.ApiUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideNetworkRepository(
        booksApi: BooksApi
    ):NetworkRepository{
        return NetworkRepository(booksApi)
    }

    @Provides
    @Singleton
    fun provideBooksApi(
       client: OkHttpClient
    ):BooksApi{
       return Retrofit.Builder()
            .baseUrl(ApiUrl.BOOKS_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }

}