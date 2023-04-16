package com.example.bookcourt.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MetricsApi {

    @POST("SendMetric")
    suspend fun sendMetric(@Body json:String)
}