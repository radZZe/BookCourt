package com.example.bookcourt.ui.recomendation

import android.app.Notification
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun Notification(count:Int,modifier: Modifier,onClick:()->Unit){
    Box(modifier =modifier.clickable { onClick() }){
        Box(modifier = Modifier
            .clip(CircleShape)
            .background(Color.Red)
            .zIndex(2f)
            .align(Alignment.TopStart),
        contentAlignment = Alignment.Center){
            Text(text = "$count", fontSize = 10.sp, color = Color.White, modifier = Modifier.padding(start = 4.dp, end = 4.dp))
        }
        NotificationImage(Modifier.zIndex(1f))
    }
}


//@Preview
//@Composable
//fun NotificationPreview(){
//    Notification(count = 10)
//}