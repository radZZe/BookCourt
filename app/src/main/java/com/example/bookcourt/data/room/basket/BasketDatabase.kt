package com.example.bookcourt.data.room.basket

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bookcourt.models.basket.BasketConverter
import com.example.bookcourt.models.basket.BasketItem

@TypeConverters(BasketConverter::class)
@Database(entities = [BasketItem::class], version = 1, exportSchema = false)
abstract class BasketDatabase:RoomDatabase() {
    abstract fun basketDao():BasketDao
}