package com.example.bookcourt.ui.basket.basketScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BasketScreen(
    viewModel: BasketViewModel = hiltViewModel()
){
    Column(modifier = Modifier.fillMaxSize()) {
        for(item in viewModel.basketItems.value){
            Text(text = item.data.bookInfo.title)
        }
    }

}