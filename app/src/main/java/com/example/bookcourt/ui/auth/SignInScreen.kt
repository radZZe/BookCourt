package com.example.bookcourt.ui.auth

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.bookcourt.ui.theme.CustomCheckBox
import com.example.bookcourt.utils.BottomBarScreen
import com.google.android.gms.location.LocationServices

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignInScreen(navController: NavController, mViewModel: SignInViewModel) {
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
            GetLocationPermission(mViewModel = mViewModel)
        }
    }
}

@Composable
fun GetLocationPermission(mViewModel: SignInViewModel) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val locationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            locationProviderClient.lastLocation.addOnSuccessListener { location ->
                mViewModel.getCity(context, location)
            }
            Log.d("AuthScreen", "PERMISSION GRANTED")
        } else {

        }
    }
    Box(
        modifier = Modifier.clickable {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    val locationProviderClient =
                        LocationServices.getFusedLocationProviderClient(context)
                    locationProviderClient.lastLocation.addOnSuccessListener { location ->
                        mViewModel.getCity(context, location)
                    }
                    Log.d("AuthScreen", "PERMISSION GRANTED")
                }
                else -> {
                    // Asking for permission
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
            .background(Color.Gray)
    ) {
        Text(text = "Check and Request Permission")
    }
}
