package com.example.bookcourt.ui.bookCard

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.data.room.basket.BasketRepositoryI
import com.example.bookcourt.models.BookDto
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.book.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BookCardViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val basketRepository: BasketRepositoryI
):ViewModel() {

    var book = mutableStateOf<Book?>(null)
    private var fetchJob: Job? = null
    val rate = mutableStateOf(0)
    val description: MutableState<String?> = mutableStateOf(null)
    val isActiveBasket = mutableStateOf(true)


    private suspend fun convertBooksJsonToList(context: Context): List<Book> {
        val json = networkRepository.getAllBooks(context)!!
        val data = Json.decodeFromString<MutableList<BookDto>>(json)
        return data.map { it.toBook() }
    }

    fun getBookByID(context: Context,id:String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val allBooksItems = convertBooksJsonToList(context)
                allBooksItems.filter {
                    id == it.isbn
                }
                book.value = allBooksItems.firstNotNullOf { item -> item.takeIf { it.isbn == id } }
                isInBasket(book.value!!)
            } catch (ioe: IOException) {
                Log.d("getAllBooks", "error cause ${ioe.cause}")
            }
        }
    }

    fun addBasketItem(item:BasketItem){
        viewModelScope.launch(Dispatchers.IO) {
            isActiveBasket.value = false
            basketRepository.addData(item)
        }
    }

    private fun isInBasket(item:Book){
        viewModelScope.launch(Dispatchers.IO) {
            val list = basketRepository.findData(item);
            isActiveBasket.value = list.isEmpty()
        }

    }


}