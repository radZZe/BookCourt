package com.example.bookcourt.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.bookcourt.data.api.MetricsApi
import com.example.bookcourt.utils.ApiUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class SendMetricsWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(
    context,
    workerParams
) {

    private val metricsApi: MetricsApi = Retrofit.Builder()
        .baseUrl(ApiUrl.METRICS_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MetricsApi::class.java)

    override suspend fun doWork(): Result {
        return try {
            val json = inputData.getString("metricJson")!!
           // metricsApi.sendMetric(json)
            Result.success()
        } catch (ex: IOException) {
            Result.retry()
        }

    }

}