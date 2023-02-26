package com.example.bookcourt.data
import android.util.Log
import com.example.bookcourt.models.Metric
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import javax.inject.Inject

class BackgroundService @Inject constructor(
    private val client: OkHttpClient
) {
    suspend fun sendMetric(metric: Metric) {
            delay(1000)
            var body = Json.encodeToString(
                serializer = Metric.serializer(),
                metric
            )
            var mediaType = "application/json".toMediaTypeOrNull();
            var requestBody = RequestBody.create(
                mediaType,
                body
            )
            val request: Request = Request.Builder()
                .url("http://c205-77-35-55-89.eu.ngrok.io/api/SendMetric")
                .method("POST", requestBody)
                .addHeader("Content-Type", "application/json")
                .build()
//            // это для тестов
            val response = client.newCall(request).execute()
            response.body?.let { Log.d("clientOk", it.string()) }
        // ИНСТРУКЦИЯ
        // 33 21 строчки ставь точки остановы
        // по тегу clientOk в logcat смотри результат отправки

    }
}