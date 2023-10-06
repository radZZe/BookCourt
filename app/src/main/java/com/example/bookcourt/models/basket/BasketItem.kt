package com.example.bookcourt.models.basket

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bookcourt.models.book.Book

@Entity(tableName = "basket_table")
data class BasketItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val data: Book,
    var amount:Int = 1,
    var isSelected:Boolean = false,
)

