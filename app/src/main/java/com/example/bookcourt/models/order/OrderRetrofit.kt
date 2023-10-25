package com.example.bookcourt.models.order

import com.example.bookcourt.models.book.BookRetrofit
import java.util.*

data class OrderRetrofit(
    val id: String,
    val title: String,
    val status: String,
    val items: List<BookRetrofit>,
    val orderDate: Date
)
