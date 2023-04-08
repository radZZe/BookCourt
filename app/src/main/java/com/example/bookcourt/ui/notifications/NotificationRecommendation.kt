package com.example.bookcourt.ui.recommendation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.CustomButton
import com.example.bookcourt.ui.theme.LightYellowBtn

@Composable
fun NotificationRecommendation(navigateToStatistics: () -> Unit, closeCallback: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(4f)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
        ) {
            Image(
                painterResource(id = R.drawable.close_icon),
                contentDescription = "",
                modifier = Modifier.clickable {
                    closeCallback()
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.winking_face),
                contentDescription = " ",
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 15.dp)
            ) {
                Text(
                    text = "Ух ты! Вы посмотрели уже 3 книги",
                    fontFamily = FontFamily(
                        Font(
                            R.font.roboto_regular,
                        )
                    ),
                    fontSize = 16.sp
                )
                Text(
                    text = "Теперь можно взглянуть на статистику", fontFamily = FontFamily(
                        Font(
                            R.font.roboto_regular,
                        )
                    ),
                    fontSize = 16.sp
                )
            }

            CustomButton(
                text = "Смотреть статистику",
                textColor = Color.Black,
                color = LightYellowBtn,
                onCLick = {
                    navigateToStatistics()
                })
        }
    }

}

@Preview(showBackground = true)
@Composable
fun NotificationRecommendationPreview(){
    NotificationRecommendation({},{})
}