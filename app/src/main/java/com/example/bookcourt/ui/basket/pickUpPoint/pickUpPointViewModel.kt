package com.example.bookcourt.ui.basket.pickUpPoint

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class pickUpPointViewModel @Inject constructor(

):ViewModel(){
    var mapSize = mutableStateOf(IntSize.Zero)
}