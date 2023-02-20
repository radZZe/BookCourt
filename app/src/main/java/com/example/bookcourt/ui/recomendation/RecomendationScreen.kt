package com.example.bookcourt.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.bookcourt.R
import com.example.bookcourt.ui.recomendation.Notification
import com.example.bookcourt.ui.recomendation.NotificationMessage
import com.example.bookcourt.ui.recomendation.RecomendationViewModel
import com.example.bookcourt.ui.theme.CustomButton
import com.example.bookcourt.ui.theme.TutorialGreeting
import com.example.bookcourt.utils.*

@Composable
fun RecomendationScreen(navController: NavController) {
    RecomendationContent(navController)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecomendationContent(
    navController: NavController,
    viewModel: RecomendationViewModel = hiltViewModel()
) {
    var context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        if (viewModel.isFirstDataLoading && viewModel.validBooks.isEmpty()) {
            viewModel.getAllBooks(context)
        }
    }

    if (viewModel.dataIsReady) {
        Column(Modifier.padding(20.dp)) {
            if (viewModel.validBooks.isNotEmpty()) {
                CardStack(
                    user = viewModel.user,
                    modifier = Modifier.fillMaxSize(),
                    itemsRaw = viewModel.validBooks,
                    onEmptyStack = {
                    },
                    onSwipeLeft = {
                        viewModel.metricSwipeLeft(it)
                    },
                    onSwipeRight = {
                        viewModel.metricSwipeRight(it)
                    },
                    onSwipeUp = {
                        viewModel.metricSwipeTop(it)
                    },
                    onSwipeDown = {
                        viewModel.metricSwipeDown(it)
                    },
                    navController = navController
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, end = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(text = "Посмотреть статистику") {
                        navController.navigate(route = Screens.Stats.route)
                    }
                }
            }

        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

}






