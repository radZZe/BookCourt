package com.example.bookcourt.ui.basket.orderingScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.room.basket.BasketRepositoryI
import com.example.bookcourt.models.basket.BasketItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderingScreenViewModel @Inject constructor(
    repositoryI: BasketRepositoryI
) : ViewModel() {

    val basketItems = mutableStateListOf<BasketItem>()
    val repository = repositoryI
    val statePayment = mutableStateOf(true) // true - Юмонеу false - СБП
    fun getItems() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getData().flowOn(Dispatchers.IO).collect{list->
                list.forEach {
                    if (it.isSelected) {
                        basketItems.add(it)
                    }
                }
            }

        }

    }

    fun getPrice():Int{
        var price = 0
        for(item in basketItems){
            price += item.data.bookInfo.price
        }
        return price
    }

    fun changeStatePayment(state:Boolean){
        statePayment.value = state
    }



}