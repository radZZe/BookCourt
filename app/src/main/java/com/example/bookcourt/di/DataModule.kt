package com.example.bookcourt.di

import android.app.Application
import android.text.BoringLayout.Metrics
import com.example.bookcourt.data.BackgroundService
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.models.Metric
import com.example.bookcourt.utils.Hashing
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
    fun provideBackgroundService(client: OkHttpClient):BackgroundService{
        return BackgroundService(client)
    }

    @Provides
    @Singleton
    fun provideMetrics(bgService: BackgroundService,dataStoreRepository: DataStoreRepository,hashing:Hashing):MetricsRepository{
        return MetricsRepository(bgService,dataStoreRepository,hashing)
    }

    @Provides
    fun provideHashing():Hashing{
        return Hashing
    }
}