package com.example.bookcourt.models.order

import com.example.bookcourt.models.book.Book

@kotlinx.serialization.Serializable
data class Order (
    val id:Int,
    val title:String,
    val status:String,
    val books:List<Book>
    )