package com.example.bookcourt.ui.auth

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.BottomBarScreen
import com.example.bookcourt.utils.Screens
import com.example.bookcourt.utils.isPermanentlyDenied
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


//@Preview
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignInScreen(navController: NavController, mViewModel: SignInViewModel) {
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthFields(navController: NavController, mViewModel: SignInViewModel) {
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
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Lead App Icon",
                modifier = Modifier.size(100.dp)
            )
            TextBlock("Имя", "Введите ваше имя", mViewModel.name) { mViewModel.onNameChanged(it) }
            Spacer(modifier = Modifier.height(18.dp))
            TextBlock(
                "Фамилия",
                "Введите вашу фамилию",
                mViewModel.surname
            ) { mViewModel.onSurnameChanged(it) }
            Spacer(modifier = Modifier.height(18.dp))
            TextBlock(
                "Телефон",
                "Введите номер телефона",
                mViewModel.phoneNumber
            ) { mViewModel.onPhoneChanged(it) }
            Spacer(modifier = Modifier.height(18.dp))
            AutoCompleteTextField("Город", "Начните вводить свой город...")
//            TextBlock("Город", "NamePlaceholder", mViewModel.name) { mViewModel.onNameChanged(it) }
            Spacer(modifier = Modifier.height(36.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brown)
                    .padding(top = 12.dp, bottom = 12.dp)
                    .clickable {
                        navController.popBackStack()
//                        navController.navigate(route = Screens.Statistics.route)
                        navController.navigate(route = BottomBarScreen.Recomendations.route)
                        mViewModel.editPrefs()
                    },
                Alignment.Center
            ) {
                Text(
                    text = "Войти",
                    color = LightBrown,
                    fontSize = 16.sp,
                    fontFamily = Gilroy,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun TextBlock(
    title: String,
    placeholder: String,
    value: String,
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
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignInScreenOld(navController: NavController, mViewModel: SignInViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = mViewModel.name,
                onValueChange = { mViewModel.onNameChanged(it) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = mViewModel.surname,
                onValueChange = { mViewModel.onSurnameChanged(it) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = mViewModel.phoneNumber,
                onValueChange = { mViewModel.onPhoneChanged(it) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            CustomCheckBox(
                text = "Запомнить меня",
                value = mViewModel.isRememberMe,
                onCheckedChange = { mViewModel.onCheckedChanged() }
            )
            Button(onClick = {
                navController.popBackStack()
                navController.navigate(route = BottomBarScreen.Recomendations.route)
                mViewModel.editPrefs()
            }) { }
            Button(onClick = {
                navController.popBackStack()
                navController.navigate(route = BottomBarScreen.Recomendations.route)
                mViewModel.editPrefs()
            }) { }
        }
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



