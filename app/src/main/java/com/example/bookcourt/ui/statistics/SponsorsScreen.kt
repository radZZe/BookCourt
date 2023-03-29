package com.example.bookcourt.ui.statistics

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookcourt.R
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.Buttons
import com.example.bookcourt.utils.Partners
import com.example.bookcourt.utils.Screens

@Composable
@Preview
fun PartnerZarya() {
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
fun PartnerIgraSlov() {
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

@Composable
fun PartnerLyuteratura(
    mViewModel: StatisticsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(LightPinkBackground)
            .clickable {
                mViewModel.sendOnClickMetric(
                    DataClickMetric(
                        Buttons.SWAP_STAT,
                        Screens.Statistics.route
                    )
                )
                mViewModel.currentScreen.value = StatisticsScreenRequest.AMOUNT_OF_BOOKS
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            TopBar(navController = navController, rq = StatisticsScreenRequest.PARTNER)

//            Image(
//                painter = painterResource(id = R.drawable.partner_lyuteratura_logo),
//                contentDescription = "lyuteratura logo",
//                modifier = Modifier
//                    .fillMaxWidth(0.5f)
//                    .fillMaxHeight(0.1f),
//                contentScale = ContentScale.Fit
//            )

            Image(
                painter = painterResource(id = R.drawable.partner_lyuteratura_content),
                contentDescription = "lyuteratura content",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                ,
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Любите детей, книги и творчество?",
                fontFamily = Inter,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                fontSize = 32.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "Тогда не проходите мимо и загляните в детский книжный магазин “Лютература”.",
                fontFamily = Inter,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Button(
                onClick = {
                    val sendIntent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            Partners.lyuteraturaUrl
                        )
                    )
                    val webIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(webIntent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(60.dp))
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(LightYellowBtn)
            ) {
                Text(text = "Заглянуть в магазин")
            }
        }
    }
}

