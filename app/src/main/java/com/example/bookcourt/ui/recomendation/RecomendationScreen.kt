package com.example.bookcourt.ui
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.bookcourt.R
import com.example.bookcourt.models.Book
import com.example.bookcourt.ui.recomendation.RecomendationViewModel
import com.example.bookcourt.ui.theme.CustomButton
import com.example.bookcourt.ui.theme.TutorialGreeting
import com.example.bookcourt.utils.CardStack
import com.example.bookcourt.utils.Screens
import com.example.bookcourt.utils.DIRECTION_TOP
import com.example.bookcourt.utils.rememberCardStackController

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
        viewModel.getAllBooks(context)
    }
    var isEmpty = viewModel.isEmpty.value

    ShowTutor(viewModel = viewModel)

    val cardStackController = rememberCardStackController()
    Column(Modifier.padding(20.dp)) {
        if (!isEmpty) {
            if (bookJson.value != null) {
                CardStack(
                    modifier = Modifier.fillMaxSize(),
                    items = bookJson.value!!, onEmptyStack = {
                        viewModel.isEmpty.value = true
                    }, cardStackController = cardStackController,
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
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, end = 24.dp),
                contentAlignment = Alignment.Center
            ) {
//                Text(text = "Книг пока что больше нет")
                CustomButton(text = "Посмотреть статистику") {
                    navController.popBackStack()
                    navController.navigate(route = Screens.Statistics.route)
                }
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

        Image(
            painter = painter,
            contentDescription = stringResource(R.string.book_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

    }
}


@Composable
fun ShowTutor(viewModel: RecomendationViewModel) {
    val tutorState = viewModel.tutorState.collectAsState(initial = true)
    AnimatedVisibility(
        visible = !tutorState.value,
        modifier = Modifier.zIndex(1f),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.55f)
                .background(Color.Black)
        )
    }
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .zIndex(2f)
            .fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = !tutorState.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            TutorialGreeting {
                viewModel.editTutorState()
            }
        }
    }

}



