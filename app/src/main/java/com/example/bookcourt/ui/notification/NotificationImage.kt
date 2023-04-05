package com.example.bookcourt.ui.recommendation

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.example.bookcourt.R

@Composable
fun NotificationImage(modifier: Modifier){
    Image(
        modifier = modifier,
        imageVector = ImageVector.vectorResource(R.drawable.notification),
        contentDescription = "Красная звезда"
    )
}

@Preview
@Composable
fun NotificationImagePreview(){
    NotificationImage(modifier = Modifier.zIndex(1f))
}