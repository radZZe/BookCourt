package com.example.bookcourt.data.room.basket

import com.example.bookcourt.models.basket.BasketItem
import kotlinx.coroutines.flow.Flow

class BasketSource(
    private val dao:BasketDao
):BasketSourceI {
    override suspend fun getData(): List<BasketItem> {
        return dao.getBasketItems()
    }

    override suspend fun addData(item: BasketItem) {
        dao.insertBook(item)
    }

    override suspend fun deleteData(item: BasketItem) {
        dao.deleteBook(item)
    }

}