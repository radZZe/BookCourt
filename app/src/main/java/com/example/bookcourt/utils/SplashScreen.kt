package com.example.bookcourt.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    mViewModel: SplashViewModel = hiltViewModel()
) {
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val rememberMeState = mViewModel.rememberMeState.collectAsState(initial = "")
    val rememberTutorState = mViewModel.rememberTutorState.collectAsState(initial = "")

    var alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000)
        if (rememberMeState.value == true && rememberTutorState.value == true) {
            navController.popBackStack()
            navController.navigate(Graph.BOTTOM_NAV_GRAPH)
        } else if (rememberMeState.value == true && rememberTutorState.value == false) {
            navController.popBackStack()
            navController.navigate(route = Screens.Tutorial.route)
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
            .background(color = BackGroundWhite)
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.book_court),
                contentDescription = "",
                modifier = Modifier
                    .size(120.dp)
                    .alpha(alpha = alpha),
                tint = AppLogoBlack
            )
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .weight(1f)
        ) {
            Text(
                text = "v0.9.5",
                fontSize = 20.sp,
                color = AppLogoBlack,
                fontFamily = Gilroy
            )
        }
    }
}