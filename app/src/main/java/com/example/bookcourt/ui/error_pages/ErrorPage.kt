package com.example.bookcourt.ui.error_pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.LightYellowBtn
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.ui.theme.Roboto

@Composable
fun ErrorPage(errorType:String,onClickButton:()->Unit){
    val header:String
    val text:String
    val buttonText:String
    val image:Int
    when (errorType) {
        ErrorType.Internet -> {
            header = ErrorTypeHeader.Internet
            text = ErrorTypeText.Internet
            buttonText = ErrorTypeButtonText.Internet
            image = R.drawable.wifi_orange
        }
        ErrorType.NotFound -> {
            header = ErrorTypeHeader.NotFound
            text = ErrorTypeText.NotFound
            buttonText = ErrorTypeButtonText.NotFound
            image = R.drawable.unknown_error
        }
        else -> {
            header = ErrorTypeHeader.UnknownError
            text = ErrorTypeText.UnknownError
            buttonText = ErrorTypeButtonText.UnknownError
            image = R.drawable.not_found
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MainBgColor)
        .padding(start = 42.dp, end = 42.dp), contentAlignment = Alignment.Center){
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxHeight(0.4f)){
            Image(painter = painterResource(id = image),contentDescription = "error image", modifier = Modifier.fillMaxSize(0.5f))
            Text(text = header,fontFamily = Roboto, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = text, fontFamily = Roboto, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onClickButton()},
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = LightYellowBtn,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(vertical = 13.dp)
            ) {
                Text(text = buttonText, color = Color.Black, fontSize = 16.sp, fontFamily = Roboto)
            }
        }

    }
}


object ErrorType{
    const val Internet = "internet"
    const val NotFound = "not found"
    const val UnknownError = "UnknownError"
}
object ErrorTypeHeader{
    const val Internet = "Нет подключения к интернету"
    const val NotFound = "Странно, но ничего нет"
    const val UnknownError = "Что-то пошло не так"
}
object ErrorTypeText{
    const val Internet = "Проверьте соединение"
    const val NotFound = "Возможно старница была удалена"
    const val UnknownError = "Попробуйте обновить страницу"
}
object ErrorTypeButtonText{
    const val Internet = "Обновить"
    const val NotFound = "На главную"
    const val UnknownError = "Обновить"
}