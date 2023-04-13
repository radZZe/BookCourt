package com.example.bookcourt.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MetricsApi {

    @Headers("Content-Type: application/json")
    @POST("SendMetric")
    fun sendMetric(@Body json:String):Call<Unit>
}