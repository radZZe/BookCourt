package com.example.bookcourt.ui.auth

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


@Composable
fun SignInScreen(
    navController: NavController,
    mViewModel: SignInViewModel = hiltViewModel()
) {
    // после прожатия кнопки показывать прогресс бар
//    var dataIsReady = mViewModel.dataIsReady
//    if(dataIsReady){
//        navController.popBackStack()
//        navController.navigate(route = BottomBarScreen.Recomendations.route)
//    }else{
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

//}

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
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Lead App Icon",
                modifier = Modifier.size(100.dp)
            )
            TextBlock("Имя", "Введите ваше имя", mViewModel.name,
                visualTransformation = VisualTransformation.None) { mViewModel.onNameChanged(it) }
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
                if(it.length<=12){
                    mViewModel.onPhoneChanged(it)
                }

            }
            Spacer(modifier = Modifier.height(18.dp))
            AutoCompleteTextField(
                "Город",
                "Начните вводить свой город...",
                mViewModel.city
            ) { mViewModel.onCityChanged(it) }
            Spacer(modifier = Modifier.height(36.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brown)
                    .padding(top = 12.dp, bottom = 12.dp)
                    .clickable {
                        Log.d("tesuto",mViewModel.name)
                        Log.d("tesuto",mViewModel.surname)
                        Log.d("tesuto",mViewModel.city)
                        if (mViewModel.name.isNotBlank()&&
                            mViewModel.surname.isNotBlank()&&
                            mViewModel.city.isNotBlank()
                        ) {
                            if (mViewModel.isValidPhone()) {
                                mViewModel.onCheckedChanged()
                                mViewModel.saveUser(navController, context)
                            } else {
                                validationState.value = false
                            }
                        }
                        else{
                            Toast.makeText(
                                context,
                                "Все поля должны быть заполненны",
                                Toast.LENGTH_SHORT
                            ).show()
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
    visualTransformation:VisualTransformation,
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GetLocationPermission(mViewModel: SignInViewModel) {
    lateinit var fusedLocationClient: FusedLocationProviderClient
    val permissionsState = rememberPermissionState(
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    permissionsState.launchPermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    when (permissionsState.permission) {
        Manifest.permission.ACCESS_COARSE_LOCATION -> {
            when {
                permissionsState.hasPermission -> {
                    val context = LocalContext.current
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            context,
                            ACCESS_COARSE_LOCATION
                        ) != PERMISSION_GRANTED
                    )
                        fusedLocationClient
                            .lastLocation
                            .addOnSuccessListener { location ->
                                mViewModel.getCity(context, location)
                            }

                }
                permissionsState.shouldShowRationale -> {
                    // Permission denied

                }
                permissionsState.isPermanentlyDenied() -> {

                }
            }
        }
    }
    Box(
        modifier = Modifier
            .background(Color.Red)
            .clickable {

            }) {
        Text(text = "Location permission")
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


