package com.example.bookcourt.ui.basket.successPurchase

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.data.room.basket.BasketRepositoryI
import com.example.bookcourt.models.BookDto
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.order.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SuccessPurchaseViewModel @Inject constructor(
 val networkRepository: NetworkRepository,
    val basketRepository: BasketRepositoryI
):ViewModel() {

    val order = mutableStateOf<Order?>(null)

    fun getOrder(){
        val listOrdersBook = mutableListOf<Book>()
        viewModelScope.launch(Dispatchers.IO) {
            basketRepository.getData().flowOn(Dispatchers.IO).collect{list->
                list.forEach{
                    if(it.isSelected){
                        listOrdersBook.add(it.data)
                    }
                }
            }

        }
        order.value = Order(
            123,
            "Заказ А-1234",
            status = "В пути",
            listOrdersBook
        )

    }

    fun mock(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val json = networkRepository.getAllBooks(context)!!
            val data = Json.decodeFromString<MutableList<BookDto>>(json)
            val res = Order(
                123,
                "Заказ А-1234",
                status = "В пути",
                data.map { it.toBook() }
            )
            order.value = res
        }
    }

}