package com.example.bookcourt.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    //---------------------VERIFICATION CODE--------------------------------------
    @POST("auth/fast")
    suspend fun authUser(@Body email: String)

    @POST("auth/fast/login")
    suspend fun verifyUser(@Body json: String)

    @GET("auth/fast/logout")
    suspend fun logOutUser()

}