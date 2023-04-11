package com.example.bookcourt.ui.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookcourt.R

@Composable
fun NotificationNothingToShow(title:String,text:String) {
    Box(
        modifier = Modifier.fillMaxSize().padding(start = 17.dp,end = 17.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.pleading_face),
                contentDescription = "pleading face"
            )
            Text(
                text = title,
                fontSize = 18.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.roboto_bold,
                    )
                )
            )
            Text(
                text = text,
                fontSize = 16.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.roboto_regular,
                    )
                )
            )
        }
    }
}