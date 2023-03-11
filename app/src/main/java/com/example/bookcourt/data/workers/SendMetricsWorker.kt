package com.example.bookcourt.data.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class SendMetricsWorker(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {
    var client = OkHttpClient().newBuilder()
        .build()
    override fun doWork(): Result {
        try {
            val json = inputData.getString("metricJson")!!
            sendMetric(json)
            return Result.success()
        } catch (ex: IOException) {
            return Result.retry()
        }
    }

    private fun sendMetric(json: String) {

        var body = json
        var mediaType = "application/json".toMediaTypeOrNull();
        var requestBody = RequestBody.create(
            mediaType,
            body
        )
//        val request: Request = Request.Builder()
//            .url("http://cca9-77-34-189-143.eu.ngrok.io/api/SendMetric")
//            .method("POST", requestBody)
//            .addHeader("Content-Type", "application/json")
//            .build()
//        val response = client.newCall(request).execute()
//        Log.d("clientOk",response.code.toString())
    }
}