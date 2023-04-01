package com.example.bookcourt.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.bookcourt.R
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.models.user.User
import com.example.bookcourt.ui.recomendation.*
import com.example.bookcourt.ui.theme.CustomButton
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardStack(
    modifier: Modifier,
    user: User,
    itemsRaw: List<Book>,
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) },
    onSwipeLeft: (item: Book) -> Unit = {},
    onSwipeRight: (item: Book) -> Unit = {},
    onSwipeUp: (item: Book) -> Unit = {},
    onSwipeDown: (item: Book) -> Unit = {},
    viewModel: CardStackViewModel = hiltViewModel(),
    onNavigateToStatistics: () -> Unit
) {
    var isEmpty = viewModel.isEmpty


    if (!isEmpty) {
        viewModel.allBooks = itemsRaw.map {
            mutableStateOf(it)
        }
        viewModel.currentItem = viewModel.allBooks.last()
        viewModel.i = viewModel.allBooks.size - 1
        var items = viewModel.allBooks
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center

        ) {
            val bookCardController = rememberBookCardController()
            BookCard(
                bookCardController,
                user,
                items[viewModel.i],
                viewModel,
                viewModel.i,
                onSwipeLeft,
                onSwipeRight,
                onSwipeUp,
                onSwipeDown,
                thresholdConfig,
            )
            if (viewModel.i != 0) {
                BookCard(
                    bookCardController,
                    user,
                    items[viewModel.i - 1],
                    viewModel,
                    viewModel.i - 1,
                    onSwipeLeft,
                    onSwipeRight,
                    onSwipeUp,
                    onSwipeDown,
                    thresholdConfig,
                )
            }
        }

    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            CustomButton(text = "Посмотреть статистику") {
                onNavigateToStatistics()
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookCard(
    bookCardController: BookCardController,
    user: User,
    item: MutableState<Book>,
    viewModel: CardStackViewModel,
    index: Int,
    onSwipeLeft: (item: Book) -> Unit = {},
    onSwipeRight: (item: Book) -> Unit = {},
    onSwipeUp: (item: Book) -> Unit = {},
    onSwipeDown: (item: Book) -> Unit = {},
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) },
) {

    var item = item.value
    var i = viewModel.i
    bookCardController.onSwipeLeft = {
        user.readBooksList.add(viewModel.allBooks.last().value)
        viewModel.updateUserStatistic(user)
        onSwipeLeft(viewModel.allBooks.last().value)
    }

    bookCardController.onSwipeRight = {
        user.readBooksList.add(viewModel.allBooks.last().value)
        viewModel.updateUserStatistic(user)
        onSwipeRight(viewModel.allBooks.last().value)
    }

    bookCardController.onSwipeUp = {
        user.wantToRead.add(viewModel.allBooks.last().value)
        viewModel.updateUserStatistic(user)
        onSwipeUp(viewModel.allBooks.last().value)

    }

    bookCardController.onSwipeDown = {
        onSwipeDown(viewModel.allBooks.last().value)
    }


    var alpha = if (index == i) bookCardController.visibility_first.value
    else if (index == i - 1) (1 - (bookCardController.visibility_first.value)) else 0f
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .zIndex(if (index == i) 3f else 1f)
    ) {
        if (index == i) {
            RecommendationIcon(
                bookCardController,
                Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(4f),
                bookCardController.wantToReadIconSize.value.dp,
                bookCardController.currentWantToReadIconColor,
                R.drawable.want_to_read_icon,
                bookCardController.wantToReadIconAlpha.value,
                "want to read icon"
            )
            RecommendationIcon(
                bookCardController,
                Modifier
                    .align(Alignment.CenterStart)
                    .zIndex(4f),
                bookCardController.dislikeIconSize.value.dp,
                bookCardController.currentDislikeIconColor,
                R.drawable.dislike_book_icon,
                bookCardController.dislikeIconAlpha.value,
                "dislike icon"
            )

            RecommendationIcon(
                bookCardController,
                Modifier
                    .align(Alignment.CenterEnd)
                    .zIndex(4f),
                bookCardController.likeIconSize.value.dp,
                bookCardController.currentLikeIconColor,
                R.drawable.like_book_icon,
                bookCardController.likeIconAlpha.value,
                "like icon"
            )
            RecommendationIcon(
                bookCardController,
                Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(4f),
                bookCardController.skipBookIconSize.value.dp,
                bookCardController.currentSkipIconColor,
                R.drawable.skip_book_icon,
                bookCardController.skipIconAlpha.value,
                "skip icon"
            )
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.80f)
                .fillMaxHeight(0.80f)
                .draggableStack(
                    controller = bookCardController,
                    thresholdConfig = thresholdConfig,
                )
                .moveTo(
                    x = if (index == i) bookCardController.offsetX.value else 0f,
                    y = if (index == i) bookCardController.offsetY.value else 0f
                )
                .graphicsLayer(
                    rotationZ = if (index == i) bookCardController.rotation.value else 0f,
                )
                .alpha(alpha)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp))
            ) {
                BookCardImage(uri = item.bookInfo.image)

            }


        }


    }

}


fun Modifier.moveTo(
    x: Float, y: Float
) = this.then(Modifier.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)

    layout(placeable.width, placeable.height) {
        placeable.placeRelative(x.roundToInt(), y.roundToInt())
    }
})



@Composable
fun BookCardImage(
    uri: String,
) {
    Box(
        modifier = Modifier, contentAlignment = Alignment.Center
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .size(Size.ORIGINAL)
                .build(),
        )

        if (painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator()
        }

        Image(
            painter = painter,
            contentDescription = stringResource(R.string.book_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )

    }
}


@Composable
fun RecommendationIcon(
    bookCardController: BookCardController,
    modifier: Modifier,
    size: Dp,
    color: Color,
    icon: Int,
    alpha: Float,
    description: String
) {
    Box(modifier = modifier) {
        Box(contentAlignment = Alignment.Center) {
            Box() {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = description,
                    modifier = Modifier
                        .size(size),
                    colorFilter = ColorFilter.tint(bookCardController.baseIconColor)
                )
            }
            Box() {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = description,
                    modifier = Modifier
                        .size(size + 20.dp)
                        .alpha(alpha / 3),
                    colorFilter = ColorFilter.tint(color)
                )
            }
            Box() {
                Image(
                    painter = painterResource(
                        id = icon
                    ),
                    contentDescription = description,
                    modifier = Modifier
                        .size(size)
                        .alpha(alpha),
                    colorFilter = ColorFilter.tint(color)
                )
            }
        }


    }
}
