package com.example.bookcourt.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.bookcourt.ui.theme.CustomCheckBox

@Composable
fun SignInScreen(navController: NavController, mViewModel: SignInViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Column() {
            CustomCheckBox(
                text = "Запомнить меня",
                value = mViewModel.isRememberMe,
                onCheckedChange = { mViewModel.onCheckedChanged() })
        }
    }
}