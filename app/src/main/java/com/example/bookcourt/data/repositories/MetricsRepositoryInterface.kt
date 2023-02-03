package com.example.bookcourt.data.repositories

import com.example.bookcourt.models.UserAction

interface MetricsRepositoryInterface {

    suspend fun onAction(action: UserAction)

    suspend fun sendUserData(name:String,surname:String,phoneNumber:String,uuid: String)

    suspend fun onClick(objectType:String)

    suspend fun onSwipe(direction:String)

    suspend fun onStartScreen()

    suspend fun onStopScreen()

    suspend fun onStartApp()

    suspend fun onStopApp()

    suspend fun screenTime()

    suspend fun appTime(sessionTime: Int)

    suspend fun getDeviceModel(): String

    suspend fun getOS(): String

    suspend fun detectShare()


}
