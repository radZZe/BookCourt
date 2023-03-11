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
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.recomendation.RecomendationViewModel
import com.example.bookcourt.ui.theme.CustomButton
import com.example.bookcourt.utils.*

@Composable
fun RecomendationScreen(onNavigateToStatistics: () -> Unit) {
    RecomendationContent(onNavigateToStatistics)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecomendationContent(
    onNavigateToStatistics: () -> Unit,
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
                        viewModel.validBooks.remove(it)
                    },
                    onSwipeRight = {
                        viewModel.metricSwipeRight(it)
                        viewModel.validBooks.remove(it)
                    },
                    onSwipeUp = {
                        viewModel.metricSwipeTop(it)
                        viewModel.validBooks.remove(it)
                    },
                    onSwipeDown = {
                        viewModel.metricSwipeDown(it)
                        viewModel.validBooks.remove(it)
                    },
                    onClick = {
                        viewModel.metricClick(it)
                    },
                    sessionTimer = { viewModel.metricScreenTime() },
                    onNavigateToStatistics = onNavigateToStatistics
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
                            DataClickMetric(
                                Buttons.OPEN_STATS,
                                Screens.Recommendation.route
                            )
                        )
                        viewModel.metricScreenTime()
//                        navController.popBackStack()
                        onNavigateToStatistics()
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






