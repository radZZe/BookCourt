package com.example.bookcourt.data.repositories

import com.example.bookcourt.models.ClickMetric
import android.content.Context
import com.example.bookcourt.models.Book
import com.example.bookcourt.models.UserAction
import java.util.UUID

interface MetricsRepositoryInterface {

//    suspend fun onAction(action: UserAction)

    suspend fun sendUserData(
        name: String,
        surname: String,
        phoneNumber: String,
        city:String,
        uuid: String,
        context:Context
    )

    suspend fun onClick(clickMetric: ClickMetric)

    suspend fun onSwipe(book: Book,direction:String)

    suspend fun appTime(sessionTime: Int, type: String,screen:String)

    suspend fun getDeviceModel(): String

    suspend fun detectShare()

}
