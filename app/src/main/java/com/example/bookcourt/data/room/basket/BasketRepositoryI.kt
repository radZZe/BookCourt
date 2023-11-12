package com.example.bookcourt.data.room.basket

import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.book.Book
import kotlinx.coroutines.flow.Flow

interface BasketRepositoryI {
    suspend fun getData() : Flow<List<BasketItem>>
    suspend fun addData(item: BasketItem)
    suspend fun deleteData(item: BasketItem)
    suspend fun updateData(item: BasketItem)

    suspend fun findData(item: Book) :List<BasketItem>

    suspend fun updateItems(items:List<BasketItem>)

    suspend fun deleteItems(items:List<BasketItem>)
}