package com.example.bookcourt.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookcourt.ui.theme.CustomCheckBox
import com.example.bookcourt.utils.BottomBarScreen

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
            }){

            }
        }
    }
}
