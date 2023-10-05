package com.example.bookcourt.ui.bottomNavigationMenu

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.room.basket.BasketRepositoryI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomNavViewModel @Inject constructor(
    val rep:BasketRepositoryI
) : ViewModel() {
    val basketSize = mutableStateOf(0)
    fun getBasketSize(){

        viewModelScope.launch(Dispatchers.IO) {
            basketSize.value = rep.getData().size
        }

    }
}