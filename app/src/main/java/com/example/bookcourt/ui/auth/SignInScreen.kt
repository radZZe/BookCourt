package com.example.bookcourt.ui.auth

import android.text.TextUtils
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.CityDropDownMenu
import com.example.bookcourt.utils.Constants
import java.util.*


@Composable
fun SignInScreen(
    onNavigateToCategorySelection: () -> Unit,
    onNavigateToTutorial: () -> Unit,
    mViewModel: SignInViewModel = hiltViewModel()
) {
    var context = LocalContext.current
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
            EmailField(value = mViewModel.email, onValueChange = { mViewModel.onEmailChanged(it) }, "Электронная почта")
            Spacer(modifier = Modifier.height(12.dp))
            CityDropDownMenu(
                mViewModel.city,
                onTFValueChange = { mViewModel.onCityChanged(it)},
                fontSize = 16,
                textWrapper = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color(239, 235, 222))
                            .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(contentAlignment = Alignment.CenterStart){
                            if (mViewModel.city.isEmpty()) {
                                Text(
                                    text = "Начните вводить свой город",
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

                        Image(
                            painter = painterResource(id = R.drawable.arrow_down),
                            contentDescription = " ",
                            modifier = Modifier.height(8.dp)
                        )
                    }
                },
                itemsFontSize = 16,
                backgroundColor = Color(239, 235, 222)
            )

        }
        Button(
            onClick = {
                mViewModel.onCheckedChanged()
                mViewModel.saveUser(onNavigateToCategorySelection,onNavigateToTutorial,context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom=20.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(50.dp),
            enabled = isValidEmail(mViewModel.email),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = LightYellowBtn,
                contentColor = Color.Black,
                disabledBackgroundColor = Color(239, 235, 222),
                disabledContentColor = Color(140, 140, 140)
            ),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Text(
                text = "Продолжить", fontSize = 16.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.roboto_regular
                    )
                ),
            )
        }
    }

}
fun isValidEmail(email:String):Boolean{
    return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
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




