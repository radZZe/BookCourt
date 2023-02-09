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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecomendationScreen(navController: NavController) {
    RecomendationContent(navController)
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecomendationContent(
    navController: NavController,
    viewModel: RecomendationViewModel = hiltViewModel()
) {

    val bookJson = viewModel.allBooks
    var context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        if(bookJson.value==null){
            viewModel.getAllBooks(context)
        }else if(bookJson.value!!.isEmpty()){
            viewModel.getAllBooks(context)
        }

    }
    var isEmpty = viewModel.isEmpty.value

    ShowTutor(viewModel = viewModel)

    val cardStackController = rememberCardStackController()
    Column(Modifier.padding(20.dp)) {
        if (!isEmpty) {
            if (bookJson.value != null) {
                CardStack(
                    modifier = Modifier.fillMaxSize(),
                    items = bookJson.value!!,
                    onEmptyStack = {
                        viewModel.isEmpty.value = true
                    }, cardStackController = cardStackController,
                    onSwipeLeft = {
                        viewModel.allBooks.value?.remove(it)
                        viewModel.metricSwipeLeft(it)
                    },
                    onSwipeRight = {
                        viewModel.allBooks.value?.remove(it)
                        viewModel.metricSwipeRight(it)
                    },
                    onSwipeUp = {
                        viewModel.allBooks.value?.remove(it)
                        viewModel.metricSwipeTop(it)
                    },
                    onSwipeDown = {
                        viewModel.allBooks.value?.remove(it)
                        viewModel.metricSwipeDown(it)
                    },
                    navController = navController
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else {
            if (!viewModel.isScreenChanged){
                navController.popBackStack()
                navController.navigate(route = Screens.Statistics.route)
                viewModel.isScreenChanged = true
            }
        }
    }
}


@Composable
fun BookCardImage(uri: String) {
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
        Row(modifier = Modifier.zIndex(1f).fillMaxSize(), horizontalArrangement = Arrangement.End) {
            NotificationMessage(Modifier.padding(top=20.dp))
            Notification(count = 5,
                Modifier
//                    .align(Alignment.TopEnd)
                    .padding(top = 100.dp)
                    .zIndex(1f))
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
//fun ShowTutor(viewModel: RecomendationViewModel) {
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



