package com.example.bookcourt.data.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.bookcourt.data.api.MetricsApi
import com.example.bookcourt.utils.ApiUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class SendMetricsWorker(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {

    private val metricsApi: MetricsApi = Retrofit.Builder()
        .baseUrl(ApiUrl.METRICS_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MetricsApi::class.java)

    override fun doWork(): Result {
        return try {
            val json = inputData.getString("metricJson")!!
            val response =  metricsApi.sendMetric(json).execute()
            Log.d("client_response",response.message())
            Result.success()
        } catch (ex: IOException) {
            Result.retry()
        }
    }

//    private  fun sendMetric(json: String) {
//        //var body = json
//       var mediaType = "application/json".toMediaTypeOrNull();
//        var requestBody = RequestBody.create(
//            mediaType,
//            body
//        )
//        //Log.d("clientOk",response.message())
//        val request: Request = Request.Builder()
//            .url("http://2f65-77-34-189-143.jp.ngrok.io/api/SendMetric")
//           .method("POST", requestBody)
//            .addHeader("Content-Type", "application/json")
//            .build()
//        val response = client.newCall(request).execute()
//        Log.d("clientOk",response.code.toString())
//    }
}