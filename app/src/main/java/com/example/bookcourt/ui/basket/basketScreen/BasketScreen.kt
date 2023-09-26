package com.example.bookcourt.ui.basket.basketScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R

@Composable
fun BasketScreen(
    viewModel: BasketViewModel = hiltViewModel()
){
    Column(modifier = Modifier.fillMaxSize()) {
        BasketTopBar()
        for(item in viewModel.basketItems.value){
            Text(text = item.data.bookInfo.title)
        }
    }

}

@Composable
fun BasketTopBar(){
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)){
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(12.dp,8.dp)
        ){
            Text(
                text = "Корзина",
                fontFamily = FontFamily(
                    Font(R.font.roboto_bold)
                ),
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterStart)
            )

        }
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(239, 235, 222)))

    }

}

