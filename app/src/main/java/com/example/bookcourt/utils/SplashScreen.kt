package com.example.bookcourt.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.bookcourt.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, mViewModel: SplashViewModel) {
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val rememberState = mViewModel.rememberMeState.collectAsState(initial = "")
    var alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000)
        if (rememberState.value == true) {
            navController.popBackStack()
            navController.navigate(route = BottomBarScreen.Recomendations.route)
        } else {
            navController.popBackStack()
            navController.navigate(route = Screens.SignIn.route)
        }
    }
    SplashUI(alpha = alphaAnim.value)
}

@Composable
fun SplashUI(alpha: Float) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = colorResource(id = R.color.main_color))
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                modifier = Modifier
                    .size(100.dp)
                    .alpha(alpha = alpha),
                imageVector = Icons.Default.Email,
                contentDescription = "",
                tint = Color.White
            )
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .weight(1f)
        ) {
            Text(
                text = "designed by BookCourt",
                fontSize = 15.sp,
                color = Color.White,

                )
        }
    }

}