package com.example.bookcourt.ui.basket.orderingScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.basket.basketScreen.BasketBookCard
import com.example.bookcourt.utils.Buttons
import com.example.bookcourt.utils.ReturnTopBar
import com.example.bookcourt.utils.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun OrderingScreen(
    onBackNavigation: () -> Unit,
    viewModel: OrderingScreenViewModel = hiltViewModel(),
    onSuccessPurchaseNavigate:()->Unit,
    onPickUpPointNavigate:()->Unit,
) {
    LaunchedEffect(key1 = Unit) {
        launch(Dispatchers.IO) {
            viewModel.getItems()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ReturnTopBar("Оформление заказа", { onBackNavigation() })
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.padding(12.dp, 0.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Адрес доставки", fontFamily = FontFamily(Font(R.font.roboto_bold)),
                    fontSize = 16.sp
                )
                Text(
                    modifier = Modifier.clickable(
                        interactionSource =  MutableInteractionSource(),
                        indication = null,
                    ) {
                        onPickUpPointNavigate()
                    },
                    text = "Изменить",
                    color = Color.Blue,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontSize = 14.sp
                )
            }
            //TODO
            Text(
                text = "г. Владивосток, ул. Крыгина 23, пункт выдачи “Wildberries”",
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "11-12 апреля ", fontFamily = FontFamily(Font(R.font.roboto_bold)),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(18.dp))
            Divider()
            Spacer(modifier = Modifier.height(18.dp))
            Column {
                Text(
                    text = "Способ оплаты", fontFamily = FontFamily(Font(R.font.roboto_bold)),
                    fontSize = 16.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = viewModel.statePayment.value,
                            onClick = { viewModel.changeStatePayment(true) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.Black,
                            )
                        )
                        Text(
                            "Картой онлайн", fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            fontSize = 16.sp
                        )
                    }
                    Image(
                        modifier = Modifier
                            .width(104.dp)
                            .height(24.dp),
                        painter = painterResource(id = R.drawable.umoney),
                        contentDescription = null
                    )
                }
                //Spacer(modifier=Modifier.height(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = !viewModel.statePayment.value,
                            onClick = { viewModel.changeStatePayment(false) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.Black,
                            )
                        )
                        Text(
                            "Перевод через СБП",
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            fontSize = 16.sp
                        )
                    }
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.sbp_logo),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                Text(
                    "Состав заказа",
                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row() {
                    viewModel.basketItems.forEach {
                        OrderItem(
                            it.data.bookInfo.price, it.data.bookInfo.image
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                    }

                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            Column() {
                Text(
                    "Состав заказа",
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${viewModel.basketItems.size} товар на сумму:",
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 16.sp
                    )
                    Text(
                        "${viewModel.getPrice()} ₽",
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Итого:",
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 20.sp
                    )
                    Text(
                        "${viewModel.getPrice()} ₽",
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(65))
                        .background(Color(252, 225, 129))
                        .padding(top = 12.dp, bottom = 12.dp)
                        .clickable(
                            interactionSource =  MutableInteractionSource(),
                            indication = null,
                        ) {
                            //onClickAddButton()
                            onSuccessPurchaseNavigate()
                            DataClickMetric(
                                Buttons.BUY_BOOK, Screens.Recommendation.route
                            )

                        }, contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Оплатить",
                            color = Color.Black,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.roboto_regular,
                                )
                            ),
                            fontSize = 16.sp,
                        )

                    }

                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("Нажимая «Оплатить» вы соглашаетесь с условиями ")
                    }
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append("политики конфидециальности")
                    }
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append(" и ")
                    }
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append(" правилами продажи ")
                    }
                })
            }
        }


    }

}


@Composable
fun OrderItem(price: Int, uri: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .width(70.dp)
        ) {
            BasketBookCard(uri)
        }

        Text(
            "$price ₽",
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontSize = 14.sp
        )
    }
}
