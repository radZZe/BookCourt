package com.example.bookcourt.data.api

import com.example.bookcourt.models.delivery.DeliveryInfo
import com.example.bookcourt.models.delivery.DeliveryResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface DeliveryApi {
    @POST("order/delivery/price")
    suspend fun getDeliveryPrice(
        @Body deliveryInfo: DeliveryInfo
    ):DeliveryResponse
}