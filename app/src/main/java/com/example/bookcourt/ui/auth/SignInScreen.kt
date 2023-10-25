package com.example.bookcourt.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.LightYellowBtn
import com.example.bookcourt.ui.theme.MainBgColor


@Composable
fun SignInScreen(
    onNavigateToVerificationCode: () -> Unit,
    mViewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBgColor)
            .padding(start = 42.dp, end = 42.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.book_court_logo),
                contentDescription = "logo",
                modifier = Modifier.height(50.dp)
            )
            Spacer(modifier = Modifier.height(50.dp))
            EmailField(
                value = mViewModel.email,
                onValueChange = { mViewModel.onEmailChanged(it) },
                "Электронная почта"
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Button(
            onClick = {
                mViewModel.saveUser(context)
                onNavigateToVerificationCode()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(50.dp),
            enabled = mViewModel.isValidEmail(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = LightYellowBtn,
                contentColor = Color.Black,
                disabledBackgroundColor = Color(239, 235, 222),
                disabledContentColor = Color(140, 140, 140)
            ),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Text(
                text = "Получить код", fontSize = 16.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.roboto_regular
                    )
                ),
            )
        }
    }

}

@Composable
fun EmailField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_regular
                )
            ),
            fontSize = 16.sp,
            color = Color.Black
        )
    ) { innerTextField ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color(239, 235, 222))
                .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 12.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    fontFamily = FontFamily(
                        Font(
                            R.font.roboto_regular
                        )
                    ),
                    fontSize = 16.sp,
                    color = Color(134, 134, 134)
                )
            }
            innerTextField()
        }
    }

}




