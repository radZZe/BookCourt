package com.example.bookcourt.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookcourt.BuildConfig
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.utils.TextRobotoRegular


@Composable
fun AboutApp(
    onNavigateToProfile: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBgColor)
    ) {
        ScreenHeader(
            text = "О приложении",
            goBack = { onNavigateToProfile() }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bookcourt_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(102.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextRobotoRegular(
                text = stringResource(id = R.string.splash_screen_version, BuildConfig.VERSION_NAME),
                color = Color.Gray,
                fontSize = 16,
            )
            TextRobotoRegular(
                text = "© 2023 Компания BookCourt ",
                color = Color.Gray,
                fontSize = 16,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 10.dp, bottom = 10.dp)
                    .clickable(interactionSource =  MutableInteractionSource(),
                        indication = null) { }, //TODO
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google_play_logo), //rate us
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(Modifier.width(12.dp))
                TextRobotoRegular(
                    text = stringResource(R.string.profile_screen_rate_us),
                    color = Color.Black,
                    fontSize = 14,
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        AboutOptions()
    }
}

@Composable
private fun AboutOptions() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(DarkBgColor)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable(interactionSource =  MutableInteractionSource(),
                    indication = null) { }
        ) {
            TextRobotoRegular(
                text = "Обработка персональных данных",
                color = Color.Black,
                fontSize = 16,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 10.dp)
                .background(Color.LightGray)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable(interactionSource =  MutableInteractionSource(),
                    indication = null) { }
        ) {
            TextRobotoRegular(
                text = "Публичная оферта",
                color = Color.Black,
                fontSize = 16,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 10.dp)
                .background(Color.LightGray)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable(interactionSource =  MutableInteractionSource(),
                    indication = null) { }
        ) {
            TextRobotoRegular(
                text = "Лицензионное соглашение",
                color = Color.Black,
                fontSize = 16,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 10.dp)
                .background(Color.LightGray)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable(interactionSource =  MutableInteractionSource(),
                    indication = null) { }
        ) {
            TextRobotoRegular(
                text = "Условия ипользования Яндекс.Карт",
                color = Color.Black,
                fontSize = 16,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}