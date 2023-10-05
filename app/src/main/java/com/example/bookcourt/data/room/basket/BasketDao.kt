package com.example.bookcourt.data.room.basket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.bookcourt.models.basket.BasketItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BasketDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBook(item:BasketItem)
    @Delete
    suspend fun deleteBook(item:BasketItem)
    @Query("SELECT * FROM basket_table")
    fun getBasketItems(): Flow<List<BasketItem>>
    @Update
    suspend fun updateItem(item:BasketItem)
}