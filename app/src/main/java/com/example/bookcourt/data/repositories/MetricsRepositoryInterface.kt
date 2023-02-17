package com.example.bookcourt.data.repositories

import android.content.Context
import com.example.bookcourt.models.Book
import com.example.bookcourt.models.UserAction
import java.util.UUID

interface MetricsRepositoryInterface {

    suspend fun onAction(action: UserAction)

    suspend fun sendUserData(name:String,surname:String,phoneNumber:String, uuid: String, context: Context)

    suspend fun onClick(objectType:String)

    suspend fun onSwipe(book: Book,direction:String)

    suspend fun onStartScreen()

    suspend fun onStopScreen()

    suspend fun onStartApp()

    suspend fun onStopApp()

    suspend fun screenTime()

    suspend fun appTime(sessionTime: Int)

    suspend fun getDeviceModel(): String

    suspend fun detectShare()

}
