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

//    var isEmpty = viewModel.isEmpty.value
//    ShowTutor(viewModel = viewModel)
//    val cardStackController = rememberCardStackController()
    if (viewModel.dataIsReady) {
        Column(Modifier.padding(20.dp)) {
            if (viewModel.validBooks.isNotEmpty()) {
                CardStack(
                    modifier = Modifier.fillMaxSize(),
                    itemsRaw = viewModel.validBooks,
                    onEmptyStack = {
//                    viewModel.isEmpty.value = true
                    },
//                    cardStackController = cardStackController,
                    onSwipeLeft = {
                        //viewModel.deleteElementFromAllBooks(it)
                        viewModel.metricSwipeLeft(it)
                    },
                    onSwipeRight = {
                        //viewModel.deleteElementFromAllBooks(it)
                        viewModel.metricSwipeRight(it)
                    },
                    onSwipeUp = {
                        //viewModel.deleteElementFromAllBooks(it)
                        viewModel.metricSwipeTop(it)
                    },
                    onSwipeDown = {
                        //viewModel.deleteElementFromAllBooks(it)
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
                        navController.popBackStack()
                        navController.navigate(route = Screens.Stats.route)
                    }
                }
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    CircularProgressIndicator()
//                }
            }

        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

}


@Composable
fun BookCardImage(
    uri: String,
    limitSwipeValue: Int,
    counter:Int,
    viewModel: CardStackViewModel,
    isNotificationDisplay: Boolean,
    navController: NavController
) {

    if (counter == limitSwipeValue) {
        viewModel.countEqualToLimit()
        // TODO
        // здесь нужно обновлять лимитное значение количества свайпов
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f), contentAlignment = Alignment.Center
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .size(Size.ORIGINAL) // Set the target size to load the image at.
                .build(),
        )

        if (painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator()
        }
        Row(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxSize(), horizontalArrangement = Arrangement.End
        ) {
            if (isNotificationDisplay) {
                NotificationMessage(Modifier.padding(top = 20.dp), counter,onClick = {
                    navController.popBackStack()
                    navController.navigate(route = Screens.StatisticsRead.route)
                })
                viewModel.countEqualToLimit()
            }
            Notification(
                count = counter,
                Modifier
//                    .align(Alignment.TopEnd)
                    .padding(top = 100.dp)
                    .zIndex(1f),
                onClick = {
                    navController.popBackStack()
//                    navController.navigate(route = Screens.StatisticsRead.route)
                    navController.navigate(route = Screens.Stats.route)
                }
            )
        }

        Image(
            painter = painter,
            contentDescription = stringResource(R.string.book_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(0f)
        )

    }
}


//@Composable
//fun ShowTutor(viewModel: RecomendationViewModel) { // dead feature
//    val tutorState = viewModel.tutorState.collectAsState(initial = true)
//    AnimatedVisibility(
//        visible = !tutorState.value,
//        modifier = Modifier.zIndex(1f),
//        enter = fadeIn(),
//        exit = fadeOut()
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .alpha(0.55f)
//                .background(Color.Black)
//        )
//    }
//    Column(
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier
//            .zIndex(2f)
//            .fillMaxSize()
//    ) {
//        AnimatedVisibility(
//            visible = !tutorState.value,
//            enter = fadeIn(),
//            exit = fadeOut()
//        ) {
//            TutorialGreeting {
//                viewModel.editTutorState()
//            }
//        }
//    }
//
//}



