package com.example.bookcourt.ui.bottomNavigationMenu

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.room.basket.BasketRepositoryI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomNavViewModel @Inject constructor(
    val rep:BasketRepositoryI
) : ViewModel() {
    val _basketSize = MutableStateFlow(0)
    val basketSize = _basketSize.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            rep.getData().flowOn(Dispatchers.IO).collect{list->
                _basketSize.update {  list.size}
            }
        }
    }
    fun getBasketSize(){



    }
}