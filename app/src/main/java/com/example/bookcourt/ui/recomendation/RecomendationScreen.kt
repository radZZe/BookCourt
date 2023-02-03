package com.example.bookcourt.ui
import android.app.Application
import androidx.compose.animation.scaleIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.bookcourt.ui.recomendation.RecomendationViewModel
import com.example.bookcourt.ui.theme.TutorialGreeting
import com.example.bookcourt.utils.CardStack
import com.example.bookcourt.utils.rememberCardStackController

@Composable
fun RecomendationScreen(navController: NavController) {
    RecomendationContent(navController = navController)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecomendationContent(viewModel: RecomendationViewModel = hiltViewModel(),
    navController: NavController) {

    val bookJson = viewModel.allBooks
    var context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllBooks(context)
    }
    var isEmpty = viewModel.isEmpty.value

    ShowTutor(viewModel = viewModel)

    val cardStackController = rememberCardStackController()
    Column(Modifier.padding(20.dp)) {

        Text(
            text = stringResource(R.string.recomendations),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 5.dp),
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        if (!isEmpty) {
            if (bookJson.value != null) {
                CardStack(
                    items = bookJson.value!!, onEmptyStack = {
                        viewModel.isEmpty.value = true
                    }, cardStackController = cardStackController,
                    navController = navController
                )
                Spacer(modifier = Modifier.padding(10.dp))
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Книг пока что больше нет")
                }
            }


        }
    }



    @Composable
    fun BookCardImage(uri: String) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 117.dp)
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
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            )
        }
    }

    @Composable
    fun IconButtonWrapper(
        modifier: Modifier,
        onClick: () -> Unit,
        img: ImageVector?,
        contentDescription: String,
        tint: Color,
        height: Dp,
        width: Dp
    ) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(
                    brush = Brush
                        .horizontalGradient(
                            colors = listOf(
                                colorResource(id = R.color.main_color),
                                colorResource(id = R.color.second_color)
                            )
                        )
                )

                .width(width)
                .height(height),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                modifier = Modifier,
                onClick = {
                    onClick()
                }
            ) {
                if (img != null) {
                    Icon(
                        img, contentDescription = contentDescription, tint = tint, modifier =
                        Modifier
                            .padding(5.dp)
                            .fillMaxSize()
                    )
                }

            }
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

