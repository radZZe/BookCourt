package com.example.bookcourt.ui.auth

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.text.TextUtils
import android.widget.Toast
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.Constants
import com.example.bookcourt.utils.PhoneNumberVisualTransformation
import com.example.bookcourt.utils.isPermanentlyDenied
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*


@Composable
fun SignInScreen(
    navController: NavController,
    mViewModel: SignInViewModel = hiltViewModel()
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.auth_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(id = R.drawable.auth_background_alpha),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f)
        )
        AuthFields(navController, mViewModel)
    }
}

@Composable
fun AuthFields(navController: NavController, mViewModel: SignInViewModel) {
    val context = LocalContext.current
    var validationState = remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .padding(start = 32.dp, end = 32.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(15.dp))
            .background(BackGroundWhite)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 20.dp)
                .verticalScroll(rememberScrollState(), reverseScrolling = true)
        ) {
            Image(
                painter = painterResource(id = R.drawable.book_court_logo),
                contentDescription = "Lead App Icon",
                modifier = Modifier.size(100.dp)
            )
            TextBlock(
                "Имя", "Введите ваше имя", mViewModel.name,
                visualTransformation = VisualTransformation.None
            ) { mViewModel.onNameChanged(it) }
            Spacer(modifier = Modifier.height(18.dp))
            TextBlock(
                "Фамилия",
                "Введите вашу фамилию",
                mViewModel.surname,
                visualTransformation = VisualTransformation.None
            ) { mViewModel.onSurnameChanged(it) }
            Spacer(modifier = Modifier.height(18.dp))
            TextBlock(
                "Телефон",
                "Введите номер телефона",
                mViewModel.phoneNumber,
                keyboardType = KeyboardType.Phone,
                visualTransformation = PhoneNumberVisualTransformation()
            ) {
                if (it.length <= 12) {
                    mViewModel.onPhoneChanged(it)
                }

            }
            Spacer(modifier = Modifier.height(18.dp))
            AutoCompleteTextField(
                "Начните вводить свой город...",
                mViewModel.city
            ) { mViewModel.onCityChanged(it) }
            Spacer(modifier = Modifier.height(36.dp))
            if (mViewModel.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Brown)
                        .padding(2.dp),
                    Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }

            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Brown)
                        .padding(top = 12.dp, bottom = 12.dp)
                        .clickable {

                            if (mViewModel.name.isNotBlank() &&
                                mViewModel.surname.isNotBlank() &&
                                mViewModel.city.isNotBlank()
                            ) {
                                if (mViewModel.isValidPhone()) {
                                    mViewModel.onCheckedChanged()
                                    mViewModel.saveUser(navController, context)
                                } else {
                                    validationState.value = false
                                }
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        "Все поля должны быть заполненны",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                    Alignment.Center
                ) {
                    Text(
                        text = "Войти",
                        color = LightBrown,
                        fontSize = 16.sp,
                        fontFamily = Gilroy,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (!validationState.value) {
                SimpleAlertDialog(validationState)
            }
        }
    }
}

@Composable
fun TextBlock(
    title: String,
    placeholder: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.Start) {
            Text(
                text = title,
                fontFamily = Gilroy,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = value,
            visualTransformation = visualTransformation,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                placeholderColor = TextPlaceHolderColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                backgroundColor = TextFieldGrey,
                cursorColor = Brown
            ),
            maxLines = 1,
            placeholder = {
                Text(
                    text = placeholder,
                    color = TextPlaceHolderColor,
                    fontFamily = Gilroy
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Next
            )
        )
    }
}

@Composable
fun SimpleAlertDialog(state: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = {
                    state.value = true
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = BrightOlive
                )
            ) {
                Text(
                    text = "Хорошо",
                    fontFamily = Gilroy,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        },
        shape = RoundedCornerShape(15.dp),
        title = {
            Text(
                text = "Неверный формат",
                fontFamily = Gilroy,
                fontSize = 18.sp,
            )
        },
        text = {
            Text(
                text = "Пожалуйста, проверьте правильность введенного номера телефона",
                fontFamily = Gilroy,
                fontSize = 16.sp,
            )
        }
    )
}

@Composable
fun NewSignInScreen(
    onNavigateToCategorySelection: () -> Unit,
    onNavigateToTutorial: () -> Unit,
//    mViewModel: SignInViewModel = hiltViewModel()
) {
    var testValue by remember { mutableStateOf("") }
    var testCity by remember { mutableStateOf("") }
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
//            EmailField(value = mViewModel.email, onValueChange = { mViewModel.onEmailChanged(it)})
            EmailField(value = testValue, onValueChange = { testValue = it }, "Электронная почта")
            AutoCompleteTextField(
                "Начните вводить свой город...",
                "Владивосток"
            ) { testCity = it }

        }
        Button(
            onClick = {
                onNavigateToCategorySelection()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom=20.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(50.dp),
            enabled = isValidEmail(testValue),
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

//@Preview(showBackground = true)
//@Composable
//fun NewSignInScreenPreview() {
//    NewSignInScreen(onNavigateToCategorySelection = { /*TODO*/ })
//}

@Composable
fun CityItem(
    title: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
            }
            .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 12.dp)
    ) {
        Text(
            text = title,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_regular
                )
            ),
            fontSize = 16.sp,
            color = Color.Black,
        )
    }
}

@Composable
fun AutoCompleteTextField(
    placeholder: String,
    value: String,
    onTFValueChange: (String) -> Unit
) {

    var textFieldValue by remember {
        mutableStateOf(value)
    }

    var isAvailable by remember {
        mutableStateOf(false)
    }

    val heightTextFields by remember {
        mutableStateOf(55.dp)
    }

    var textFieldsSize by remember {
        mutableStateOf(Size.Zero)
    }

    var isClearIconBtnVisible by remember {
        mutableStateOf(false)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current

    Spacer(modifier = Modifier.height(10.dp))
    BasicTextField(
        value = textFieldValue,
        enabled = isAvailable,
        onValueChange = {
            textFieldValue = it
            //expanded = true
            onTFValueChange(it)
            expanded = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = true),
                onClick = { expanded = !expanded }
            )
            .onGloballyPositioned {
                textFieldsSize = it.size.toSize()
            }
            .focusRequester(focusRequester)
            .size(48.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color(239, 235, 222))
                .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
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
            Image(
                painter = painterResource(id = R.drawable.arrow_down),
                contentDescription = " ",
                modifier = Modifier.height(8.dp)
            )
        }
    }
    AnimatedVisibility(visible = expanded) {
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(15.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 150.dp)
                    .zIndex(2f)
                    .background(Color(239, 235, 222))
            ) {
                items(
                    if (isAvailable) {
                        Constants.cities.apply {
                            filterNot {
                                it.contains(Constants.OTHER_CITY)
                            }
                            filter {
                                it.lowercase()
                                    .contains(textFieldValue.lowercase()) || it.lowercase()
                                    .contains("others")
                            }
                            sorted()
                        }
                    } else {
                        Constants.cities.sorted().also {
                            Collections.swap(it, it.indexOf(Constants.OTHER_CITY), it.lastIndex)
                        }
                    }

                ) {
                    CityItem(title = it) { title ->
                        if (title == Constants.OTHER_CITY) {
                            isAvailable = true
                            // isClearIconBtnVisible = true
                            focusRequester.requestFocus()
                            textFieldValue = ""
                            onTFValueChange(textFieldValue)
                        } else {
                            onTFValueChange(title)
                            textFieldValue = title
                            onTFValueChange(textFieldValue)
                        }
                        isClearIconBtnVisible = true
                        expanded = false
                    }
                    if (isAvailable) {
                        focusRequester.requestFocus()
                    }
                }
            }
        }
    }
}

