package com.example.bookcourt.ui.basket.emptyBasketScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookcourt.R
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.utils.Buttons
import com.example.bookcourt.utils.Screens


@Composable
fun EmptyBasketScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White).
        padding(12.dp,8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(R.drawable.ic_empty_basket),
            contentDescription = null
        )
        Text(
            textAlign = TextAlign.Center ,
            text = "В корзине ничего нет",
            fontFamily = FontFamily(Font(R.font.roboto_bold)),
            fontSize = 18.sp
        )
        Text(
            modifier = Modifier.fillMaxWidth(0.7f),
            textAlign = TextAlign.Center ,
            text = "Воспользуйтесь рекомендациями или библиотекой",
            fontFamily = FontFamily(Font(R.font.roboto_regular)),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(22.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(48.dp)
                .clip(RoundedCornerShape(65))
                .background(Color(252, 225, 129))
                .padding(top = 12.dp, bottom = 12.dp)
                .clickable(
                    interactionSource =  MutableInteractionSource(),
                    indication = null,
                ) {


                },
            contentAlignment = Alignment.Center
        ) {
            Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Подбор книг",
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
    }
}

@Preview
@Composable
fun PreviewEmptyBasketScreen() {
    EmptyBasketScreen()
}