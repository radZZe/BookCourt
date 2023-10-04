package com.example.bookcourt.data.room.basket

import com.example.bookcourt.models.basket.BasketItem
import kotlinx.coroutines.flow.Flow

interface BasketSourceI {
    suspend fun getData() : List<BasketItem>
    suspend fun addData(item: BasketItem)
    suspend fun deleteData(item: BasketItem)
}