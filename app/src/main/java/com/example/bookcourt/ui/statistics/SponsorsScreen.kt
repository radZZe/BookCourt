package com.example.bookcourt.ui.statistics


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.Partners

@Composable
@Preview
fun PartnerZarya() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ZaryaBackground)
            .padding(bottom = 20.dp),
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
                fontSize = 26.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "В ЦСИ \"Заря\" вы всегда найдете познавателььную и обучающую литературу, связанную с искусством.",
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                fontSize = 16.sp
            )
        }
        RedirectButton(context = context, redirectUrl = Partners.csiZaryaUrl)
    }
}

@Composable
fun PartnerIgraSlov() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(IgraSlovBackground)
            .padding(bottom = 20.dp),
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
                fontSize = 26.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Независимый книжный магазин \"Игра слов\", место, где редкие книги и кофе.",
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                fontSize = 16.sp
            )
        }
        RedirectButton(context = context, redirectUrl = Partners.igraSlovUrl)
    }
}

@Composable
fun PartnerLyuteratura() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightPinkBackground)
            .padding(bottom = 20.dp, top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        Image(
            painter = painterResource(id = R.drawable.partner_lyuteratura_logo),
            contentDescription = "lyuteratura logo",
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.1f),
            contentScale = ContentScale.Fit
        )
        Image(
            painter = painterResource(id = R.drawable.partner_lyuteratura_content),
            contentDescription = "lyuteratura content",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "Любите детей, книги и творчество?",
            fontFamily = Inter,
            fontWeight = FontWeight.Black,
            color = Color.Black,
            fontSize = 26.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = "Тогда не проходите мимо и загляните в детский книжный магазин “Лютература”.",
            fontFamily = Inter,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        RedirectButton(context = context, redirectUrl = Partners.lyuteraturaUrl)
    }
}

