package com.example.bookcourt.data.api

import com.example.bookcourt.models.book.BookRetrofit
import com.example.bookcourt.models.order.OrderRetrofit
import com.example.bookcourt.models.user.Notification
import com.example.bookcourt.models.user.User
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @GET("user/")
    suspend fun getUser() : User

    @POST("user/")
    suspend fun saveUserData(json: String)

    @GET("user/book/want")
    suspend fun getWantToReadBooks() : List<BookRetrofit>

    @GET("user/book/liked")
    suspend fun getLikedBooks() : List<BookRetrofit>

    @GET("user/book/readed")
    suspend fun getReadBooks() : List<BookRetrofit>

    @GET("user/notification")
    suspend fun getUserNotifications() : List<Notification>

    @POST("user/notification/viewed")
    suspend fun markNotification(id: String)

    @GET("user/order")
    suspend fun getUserOrders() : List<OrderRetrofit>

    @POST("user/order/cancel")
    suspend fun cancelOrder(orderId: String, reasonType: String, reasonText: String)


}