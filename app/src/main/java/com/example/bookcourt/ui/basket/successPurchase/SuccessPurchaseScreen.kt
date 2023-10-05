package com.example.bookcourt.ui.basket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.ui.basket.successPurchase.OrderWidget
import com.example.bookcourt.ui.basket.successPurchase.SuccessPurchaseViewModel
import com.example.bookcourt.ui.theme.GrayBackground
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.ui.theme.Roboto
import com.example.bookcourt.utils.CustomButton
import com.example.bookcourt.utils.ReturnTopBar

@Composable
fun SuccessPurchaseScreen (
    viewModel: SuccessPurchaseViewModel = hiltViewModel(),
    onNavigateBack:()->Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        //viewModel.mock(context)
        viewModel.getOrder()
    }

    if(viewModel.order.value!=null){
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(MainBgColor)
                .verticalScroll(rememberScrollState())
        ){
            ReturnTopBar(curScreenName = "Ваш заказ оформлен") {
                onNavigateBack()
            }
            Image(
                painter = painterResource(id = R.drawable.success_img),
                contentDescription = "success image",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(100.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OrderWidget(viewModel.order.value!!)
                Text( text = "Подробная информация о статусе заказа и месте получения доступна в профиле пользователя.",
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    fontSize = 16.sp
                )
                CustomButton(
                    text = "Узнать статус заказа",
                    modifier = Modifier.fillMaxWidth(),
                    color = GrayBackground,
                    textColor = Color.Black,
                    onCLick = {
                        //todo
                    }
                )
            }
        }
    }

}

