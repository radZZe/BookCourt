package com.example.bookcourt.data.room.basket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.book.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BasketDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBook(item:BasketItem)
    @Delete
    suspend fun deleteItem(item:BasketItem)
    @Query("SELECT * FROM basket_table")
    fun getBasketItems(): Flow<List<BasketItem>>
    @Update
    suspend fun updateItem(item:BasketItem)
    @Query("SELECT * FROM basket_table WHERE data = :data")
    fun findItem(data: Book): List<BasketItem>
    @Update
    suspend fun updateItems(items:List<BasketItem>)

    @Delete
    suspend fun deleteItems(items:List<BasketItem>)
}