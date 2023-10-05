package com.example.bookcourt.data.room.basket

import com.example.bookcourt.models.basket.BasketItem
import kotlinx.coroutines.flow.Flow

interface BasketSourceI {
    suspend fun getData() : Flow<List<BasketItem>>
    suspend fun addData(item: BasketItem)
    suspend fun deleteData(item: BasketItem)
    suspend fun updateData(item: BasketItem)
}