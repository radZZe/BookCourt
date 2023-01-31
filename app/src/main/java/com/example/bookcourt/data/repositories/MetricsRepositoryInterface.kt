package com.example.bookcourt.data.repositories

interface MetricsRepositoryInterface {

    suspend fun sendUserData(name:String,surname:String,phoneNumber:String,uuid: String)
    suspend fun onClick(objectType:String)

    suspend fun onSwipe(direction:String)

    suspend fun onStartScreen()

    suspend fun onStopScreen()

    suspend fun onStartApp()

    suspend fun onStopApp()

    suspend fun screenTime()

    suspend fun appTime(sessionTime: Int)

}
