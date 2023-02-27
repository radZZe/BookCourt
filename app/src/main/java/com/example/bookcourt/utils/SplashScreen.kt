package com.example.bookcourt.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.bookcourt.R
import com.example.bookcourt.ui.recomendation.RecomendationViewModel
import com.example.bookcourt.ui.theme.Brown
import com.example.bookcourt.ui.theme.Gilroy
import com.example.bookcourt.ui.theme.LightBrown
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    mViewModel: SplashViewModel = hiltViewModel()
) {
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
            navController.navigate(route = Screens.Recommendation.route)
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
            .background(color = Brown)
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "",
                modifier = Modifier
                    .size(120.dp)
                    .alpha(alpha = alpha),
                tint = LightBrown
            )
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .weight(1f)
        ) {
//            Text(
//                text = "designed by BookCourt",
//                fontSize = 20.sp,
//                color = LightBrown,
//                fontFamily = Gilroy
//            )
            Text(
                text = "v0.9.5",
                fontSize = 20.sp,
                color = LightBrown,
                fontFamily = Gilroy
            )
        }
    }

}