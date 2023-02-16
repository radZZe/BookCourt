package com.example.bookcourt.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.bookcourt.models.Metric
import com.example.bookcourt.models.UserDataMetric
import kotlinx.coroutines.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import javax.inject.Inject

class BackgroundService @Inject constructor(
    private val client: OkHttpClient
) {
//
//    var stack = mutableStateListOf<Metric>()
//

    fun sendMetric(metric: Metric) {
        GlobalScope.launch(Dispatchers.IO) {
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
                .url("https://bookcourttest-ee89c-default-rtdb.asia-southeast1.firebasedatabase.app/testMetric.json")
                .method("POST", requestBody)
                .addHeader("Content-Type", "application/json")
                .build()
            val response = client.newCall(request).execute()
            Log.d("clientOk", response.code.toString())

        }
    }
}