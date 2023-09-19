package com.example.bookcourt.ui.recommendation

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.bookcourt.R
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.user.User
import com.example.bookcourt.ui.theme.Roboto
import com.example.bookcourt.utils.CustomButton
import kotlin.math.roundToInt
import com.example.bookcourt.utils.Constants.LIMIT_WINDOW_HEIGHT

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardStack(
    modifier: Modifier,
    user: User,
    frontItem: Book?,
    backItem: Book?,
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) },
    onSwipeLeft: (item: Book) -> Unit = {},
    onSwipeRight: (item: Book) -> Unit = {},
    onSwipeUp: (item: Book) -> Unit = {},
    onSwipeDown: (item: Book) -> Unit = {},
    viewModel: RecommendationViewModel = hiltViewModel(),
    disableDraggable: Boolean,
) {

    if (frontItem != null) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center

        ) {
            val bookCardController = rememberBookCardController()
            BookCard(
                bookCardController,
                user,
                frontItem,
                viewModel,
                true,
                onSwipeLeft,
                onSwipeRight,
                onSwipeUp,
                onSwipeDown,
                thresholdConfig,
                disableDraggable
            )
            if (backItem != null) {
                BookCard(
                    bookCardController,
                    user,
                    backItem,
                    viewModel,
                    false,
                    onSwipeLeft,
                    onSwipeRight,
                    onSwipeUp,
                    onSwipeDown,
                    thresholdConfig,
                    disableDraggable
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
//                viewModel.metricClick(
//                    DataClickMetric(
//                        Buttons.OPEN_STATS,
//                        Screens.Recommendation.route
//                    )
//                )
//                viewModel.metricScreenTime()
//                onNavigateToStatistics()
            }
        }
    }


}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookCard(
    bookCardController: BookCardController,
    user: User,
    item: Book,
    viewModel: RecommendationViewModel,
    isFrontItem: Boolean,
    onSwipeLeft: (item: Book) -> Unit = {},
    onSwipeRight: (item: Book) -> Unit = {},
    onSwipeUp: (item: Book) -> Unit = {},
    onSwipeDown: (item: Book) -> Unit = {},
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) },
    disableDraggable: Boolean,
) {

    if (isFrontItem) {
        bookCardController.onSwipeLeft = {
            user.readBooksList.add(item)
            viewModel.updateUserStatistic(user)
            onSwipeLeft(item)
        }

        bookCardController.onSwipeRight = {
            user.readBooksList.add(item)
            viewModel.updateUserStatistic(user)
            onSwipeRight(item)
        }

        bookCardController.onSwipeUp = {
            user.wantToRead.add(item)
            viewModel.updateUserStatistic(user)
            onSwipeUp(item)

        }

        bookCardController.onSwipeDown = {
            onSwipeDown(item)
        }
    }


    val alpha = when (isFrontItem) {
        true -> bookCardController.visibilityFirst.value
        false -> (1 - (bookCardController.visibilityFirst.value))
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .zIndex(if (isFrontItem) 3f else 1f),
    ) {

        if (isFrontItem) {
            RecommendationIcon(
                Modifier
                    .zIndex(4f)
                    .align(Alignment.TopCenter),
                bookCardController.wantToReadIconSize.value.dp,
                bookCardController.currentWantToReadIconColor,
                R.drawable.want_to_read_icon,
                bookCardController.wantToReadIconAlpha.value,
                "want to read icon"
            )

            RecommendationIcon(
                Modifier
                    .zIndex(4f)
                    .align(Alignment.CenterStart),
                bookCardController.dislikeIconSize.value.dp,
                bookCardController.currentDislikeIconColor,
                R.drawable.dislike_book_icon,
                bookCardController.dislikeIconAlpha.value,
                "dislike icon"
            )

            RecommendationIcon(
                Modifier
                    .zIndex(4f)
                    .align(Alignment.CenterEnd),
                bookCardController.likeIconSize.value.dp,
                bookCardController.currentLikeIconColor,
                R.drawable.like_book_icon,
                bookCardController.likeIconAlpha.value,
                "like icon"
            )


            RecommendationIcon(
                Modifier
                    .zIndex(4f)
                    .align(Alignment.BottomCenter),
                bookCardController.skipBookIconSize.value.dp,
                bookCardController.currentSkipIconColor,
                R.drawable.skip_book_icon,
                bookCardController.skipIconAlpha.value,
                "skip icon"
            )

        }


        val windowHeight =
            LocalConfiguration.current.screenHeightDp.toFloat() * LocalDensity.current.density



        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = if (!disableDraggable) {
                Modifier
                    .width(300.dp)
                    .height(if (windowHeight > LIMIT_WINDOW_HEIGHT) 470.dp else 400.dp)
                    .draggableStack(
                        controller = bookCardController,
                        thresholdConfig = thresholdConfig,
                    )
                    .moveTo(
                        x = if (isFrontItem) bookCardController.offsetX.value else 0f,
                        y = if (isFrontItem) bookCardController.offsetY.value else 0f
                    )
                    .graphicsLayer(
                        rotationZ = if (isFrontItem) bookCardController.rotation.value else 0f,
                    )
                    .alpha(alpha)
                    .align(Alignment.Center).shadow(
                        elevation = 20.dp,
                        spotColor = Color(30, 173, 0, 255),
                        shape = RoundedCornerShape(20.dp)
                    )
            } else {
                Modifier
                    .width(300.dp)
                    .height(if (windowHeight > LIMIT_WINDOW_HEIGHT) 470.dp else 400.dp)
                    .alpha(alpha)
                    .align(Alignment.Center).shadow(
                        elevation = 20.dp,
                        spotColor = Color(30, 173, 0, 255),
                        shape = RoundedCornerShape(20.dp)
                    )
            }
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
    isBookCardScreen:Boolean = false
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
        Box(
            modifier = Modifier
                .fillMaxWidth(if (isBookCardScreen) 0.4f else 0.32f)
                .zIndex(3f)
                .align(
                    Alignment.BottomStart
                )
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(40))
                    .background(Color.White)
                    .padding(start = 6.dp, end = 6.dp, top = 5.dp, bottom = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.green_rating),
                        contentDescription = null,
                        modifier = Modifier.size(if (isBookCardScreen) 16.dp else 21.dp)
                    )
                    Spacer(modifier = Modifier.width(if (isBookCardScreen) 3.dp else 5.dp))
                    Text(
                        text = "5.0",
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isBookCardScreen) 14.sp else 18.sp,
                        color = Color(48, 178, 93)
                    )
                }
            }
        }

        Image(
            painter = painter,
            contentDescription = stringResource(R.string.book_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
        )

    }
}


@Composable
fun RecommendationIcon(
    modifier: Modifier,
    size: Dp,
    color: Color,
    icon: Int,
    alpha: Float,
    description: String
) {
    Box(modifier = modifier) {
        Box(contentAlignment = Alignment.Center) {
            Box {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = description,
                    modifier = Modifier
                        .size(size),
                    colorFilter = ColorFilter.tint(Color(222, 210, 169))
                )
            }
            Box {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = description,
                    modifier = Modifier
                        .size(size + 20.dp)
                        .alpha(alpha / 3),
                    colorFilter = ColorFilter.tint(color)
                )
            }
            Box {
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


