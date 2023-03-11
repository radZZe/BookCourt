package com.example.bookcourt.data.repositories

import com.example.bookcourt.models.metrics.DataClickMetric
import android.content.Context
import com.example.bookcourt.models.book.Book

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

    suspend fun onClick(clickMetric: DataClickMetric)

    suspend fun onSwipe(book: Book, direction:String)

    suspend fun appTime(sessionTime: Int, type: String,screen:String)

    suspend fun getDeviceModel(): String

    suspend fun detectShare()

}
