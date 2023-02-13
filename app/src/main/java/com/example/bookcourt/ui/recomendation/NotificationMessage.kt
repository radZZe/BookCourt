package com.example.bookcourt.ui.recomendation

import android.app.Notification
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookcourt.utils.TriangleEdgeShape

@Composable
fun NotificationMessage(modifier: Modifier,count:Int,onClick:()->Unit) {
    Row(modifier.height(IntrinsicSize.Max).clickable { onClick() }) {
        Column(
            modifier = Modifier.background(
                color = Color(0xFFD9D9D9),
                shape = RoundedCornerShape(10.dp, 10.dp, 0.dp, 10.dp)
            ).fillMaxWidth(0.8f)
        ) {
            Text( modifier = Modifier.padding(5.dp),
                text = "Ух ты! Ты просвайпал уже $count книг. Теперь можно посмотреть интересную статистику ;)\n" +
                        "Когда захочешь - просто нажми на меня!"
            )
        }
        Column(
            modifier = Modifier.background(
                color = Color(0xFFD9D9D9),
                shape = TriangleEdgeShape(20)
            )
                .width(8.dp)
                .fillMaxHeight()
        ) {
        }
    }
}

//@Preview
//@Composable
//fun NotificationMessagePreview(){
//    NotificationMessage()
//}