package com.example.bookcourt.ui.basket.successPurchase

import android.content.Context
import androidx.compose.runtime.*
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

    var option by mutableStateOf<OrderOptions>(OrderOptions.ACTIVE)

    var reason by mutableStateOf("")

    fun onOptionChanged(newText: OrderOptions) {
        option = newText
    }

    fun onReasonChanged(newText: String) {
        reason = newText
    }

    var checkbox1State by mutableStateOf(false)
    var checkbox2State by mutableStateOf(false)
    var checkbox3State by mutableStateOf(false)

    fun onCheckbox1StateChange() {
        checkbox1State = !checkbox1State
        checkbox2State = false
        checkbox3State = false
    }

    fun onCheckbox2StateChange() {
        checkbox2State = !checkbox2State
        checkbox1State = false
        checkbox3State = false
    }

    fun onCheckbox3StateChange() {
        checkbox3State = !checkbox3State
        checkbox2State = false
        checkbox1State = false
    }

    fun sendDeclineOrderReason() {
        if (checkbox1State) {
            //TODO "Уже купил книгу по более низкой цене"
        } else if (checkbox2State) {
            //TODO "Перехотел читать эту книгу"
        } else if (checkbox3State) {
            //TODO reason
        }
    }

}

enum class OrderOptions {
    ACTIVE, BOUGHT
}