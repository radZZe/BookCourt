package com.example.bookcourt.data.room.basket

import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.book.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BasketRepository @Inject constructor(
    private val source: BasketSource):BasketRepositoryI {
    override suspend fun getData(): Flow<List<BasketItem>> {
        return source.getData()
    }

    override suspend fun addData(item: BasketItem) {
        return source.addData(item)
    }

    override suspend fun deleteData(item: BasketItem) {
        return source.deleteData(item)
    }

    override suspend fun updateData(item: BasketItem) {
        return source.updateData(item)
    }

    override suspend fun findData(item: Book):List<BasketItem> {
        return source.findData(item)
    }

}