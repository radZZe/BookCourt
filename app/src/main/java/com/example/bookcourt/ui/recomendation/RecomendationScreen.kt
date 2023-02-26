package com.example.bookcourt.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookcourt.models.ClickMetric
import com.example.bookcourt.ui.recomendation.RecomendationViewModel
import com.example.bookcourt.ui.theme.CustomButton
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
                    onClick = {
                        viewModel.metricClick(it)
                    },
                    sessionTimer = { viewModel.metricScreenTime() },
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
                        viewModel.metricClick(
                            ClickMetric(
                                Buttons.OPEN_STATS,
                                Screens.Recommendation.route
                            )
                        )
                        viewModel.metricScreenTime()
                        navController.navigate(route = Screens.Statistics.route)
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






