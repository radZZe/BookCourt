package com.example.bookcourt.models.library

import androidx.compose.runtime.MutableState
import com.example.bookcourt.models.book.BookRetrofit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
@Serializable
data class BookBlock (
    val blockName:String,
    var blockItems:List<BookRetrofit>
)