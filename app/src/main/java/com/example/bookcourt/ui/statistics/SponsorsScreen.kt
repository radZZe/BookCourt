package com.example.bookcourt.ui.statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.CustomButton
import com.example.bookcourt.ui.theme.IgraSlovBackground
import com.example.bookcourt.ui.theme.ZaryaBackground

@Composable
@Preview
fun Zarya() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZaryaBackground)
            .padding(bottom = 40.dp, top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.csi_zarya_logo),
            contentDescription = "Zarya logo",
            modifier = Modifier.size(130.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.csi_zarya_screen_cover),
            contentDescription = "Zarya cover",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Книги про искусство",
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
//                text = "В ЦСИ \"Заря\" вы всегда найдете\nпознавателььную и обучающую литературу,\nсвязанную с искусством.",
                text = "В ЦСИ \"Заря\" вы всегда найдете познавателььную и обучающую литературу, связанную с искусством.",
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                fontSize = 18.sp
            )
        }
        CustomButton(
            text = "Заглянуть в магазин",
            textColor = Color.Black,
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFFCE181)
        )
    }
}

@Composable
//@Preview
fun IgraSlov() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(IgraSlovBackground)
            .padding(bottom = 40.dp, top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.igra_slov_logo_unfilled),
            contentDescription = "Zarya logo",
            modifier = Modifier.size(130.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.igra_slov_screen_cover),
            contentDescription = "Zarya cover",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Вы точно найдете, что почитать",
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Независимый книжный магазин \"Игра слов\", место, где редкие книги и кофе.",
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                fontSize = 18.sp
            )
        }
        CustomButton(
            text = "Заглянуть в магазин",
            textColor = Color.Black,
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFFCE181)
        )
    }
}