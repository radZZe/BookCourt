package com.example.bookcourt.data.room.basket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.bookcourt.models.basket.BasketItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BasketDao {
    @Upsert
    suspend fun insertBook(item:BasketItem)
    @Delete
    suspend fun deleteBook(item:BasketItem)
    @Query("SELECT * FROM basket_table")
    suspend fun getBasketItems(): List<BasketItem>
}