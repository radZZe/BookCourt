package com.example.bookcourt.data.room.basket

import com.example.bookcourt.models.basket.BasketItem
import kotlinx.coroutines.flow.Flow

class BasketSource(
    private val dao:BasketDao
):BasketSourceI {
    override suspend fun getData(): Flow<List<BasketItem>> {
        return dao.getBasketItems()
    }

    override suspend fun addData(item: BasketItem) {
        //var list = dao.getBasketItems()
        dao.insertBook(item)
//        if(item !in list){
//            dao.insertBook(item)
//        }else{
//            item.amount = item.amount + 1
//            dao.updateItem(item)
//        }

    }

    override suspend fun deleteData(item: BasketItem) {
        dao.deleteBook(item)
    }

    override suspend fun updateData(item: BasketItem){
        dao.updateItem(item)
    }

}