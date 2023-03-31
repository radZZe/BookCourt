package com.example.bookcourt.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
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
import com.example.bookcourt.models.book.BookInfo
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.models.user.User
import com.example.bookcourt.ui.CardInfoScreen
import com.example.bookcourt.ui.recomendation.*
import com.example.bookcourt.ui.theme.Manrope
import com.example.bookcourt.ui.theme.CustomButton
import com.example.bookcourt.utils.MetricsUtil.convertPixelsToDp
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
    onClick: (clickMetric: DataClickMetric) -> Unit = {},
    sessionTimer: () -> Unit = {},
    viewModel: CardStackViewModel = hiltViewModel(),
    onNavigateToStatistics: () -> Unit
) {
    var limitSwipeValue = 3
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
                limitSwipeValue,
                viewModel.i,
                onSwipeLeft,
                onSwipeRight,
                onSwipeUp,
                onSwipeDown,
                { onClick(it) },
                thresholdConfig,
                sessionTimer,
                onNavigateToStatistics
            )
            if (viewModel.i != 0) {
                BookCard(
                    bookCardController,
                    user,
                    items[viewModel.i - 1],
                    viewModel,
                    limitSwipeValue,
                    viewModel.i - 1,
                    onSwipeLeft,
                    onSwipeRight,
                    onSwipeUp,
                    onSwipeDown,
                    { onClick(it) },
                    thresholdConfig,
                    sessionTimer,
                    onNavigateToStatistics
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
    itemRaw: MutableState<Book>,
    viewModel: CardStackViewModel,
    limitSwipeValue: Int,
    index: Int,
    onSwipeLeft: (item: Book) -> Unit = {},
    onSwipeRight: (item: Book) -> Unit = {},
    onSwipeUp: (item: Book) -> Unit = {},
    onSwipeDown: (item: Book) -> Unit = {},
    onClick: (clickMetric: DataClickMetric) -> Unit = {},
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) },
    sessionTimer: () -> Unit = {},
    onNavigateToStatistics: () -> Unit
) {

    var item = itemRaw.value
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
        else if (index == i - 1) (1-(bookCardController.visibility_first.value)) else 0f
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .zIndex(if (index == i) 3f else 1f)
        ) {
            if(index == i){
                RecomendationIcon(
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
                RecomendationIcon(
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

                RecomendationIcon(
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
                RecomendationIcon(
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
                    BookCardImage(
                        uri = item.bookInfo.image,
                        limitSwipeValue = limitSwipeValue,
                        counter = if (index == viewModel.i - 1 || index == viewModel.i) {
                            viewModel.counter
                        } else 0,
                        viewModel = viewModel,
                        onClick = {
                            onClick(
                                it
                            )
                            sessionTimer()
                        },
                        onNavigateToStatistics = onNavigateToStatistics
                    )

//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(top = 30.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
//                        verticalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Column(
//                                verticalArrangement = Arrangement.SpaceAround,
//                                modifier = Modifier.fillMaxWidth(0.7f)
//                            ) {
//                                Text(
//                                    text = item.bookInfo.title,
//                                    color = Color.White,
//                                    fontSize = 19.sp,
//                                    fontFamily = FontFamily(
//                                        Font(
//                                            R.font.manrope_extrabold, weight = FontWeight.W600
//                                        )
//                                    ),
//                                    maxLines = 1,
//                                    overflow = TextOverflow.Ellipsis
//                                )
//                                Spacer(modifier = Modifier.size(5.dp))
//                                Text(
//                                    text = item.bookInfo.author,
//                                    color = Color(0xFFA3A3A3),
//                                    fontSize = 16.sp,
//                                    fontFamily = FontFamily(
//                                        Font(
//                                            R.font.manrope_medium, weight = FontWeight.W600
//                                        )
//                                    )
//                                )
//                                Spacer(modifier = Modifier.size(5.dp))
//                                Text(
//                                    modifier = Modifier,
//                                    text = item.bookInfo.genre,
//                                    color = Color(0xFFFFFFFF),
//                                    fontSize = 14.sp,
//                                    fontFamily = FontFamily(
//                                        Font(
//                                            R.font.manrope_medium, weight = FontWeight.W600
//                                        )
//                                    )
//                                )
//
//                            }
//                            Image(
//                                painter = painterResource(id = R.drawable.igra_slov_logo),
//                                contentDescription = "Logo image",
//                                modifier = Modifier
//                                    .size(70.dp)
//                                    .clip(CircleShape)
//                            )
//                        }
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clip(RoundedCornerShape(50.dp))
//                                .background(Color(0xFF8BB298))
//                                .clickable {
//                                    onClick(
//                                        DataClickMetric(
//                                            Buttons.BUY_BOOK, Screens.Recommendation.route
//                                        )
//                                    )
//                                    val sendIntent: Intent = Intent(
//                                        Intent.ACTION_VIEW, Uri.parse(
//                                            item.buyUri
//                                        )
//                                    )
//                                    val webIntent = Intent.createChooser(sendIntent, null)
//                                    context.startActivity(webIntent)
//                                },
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                modifier = Modifier.padding(10.dp), text = "Купить", color = Color(
//                                    0xFFFFFFFF
//                                )
//                            )
//                        }
//                    }

                }


            }


        }


//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
////            verticalArrangement = Arrangement.SpaceAround,
//            modifier = Modifier.zIndex(if (index == i) 2f else 0f)
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.08f)
//                , contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    modifier = Modifier.size(100.dp),
//                    painter = painterResource(id = R.drawable.want_to_read_icon),
//                    contentDescription = "want to read icon",
//                )
//            }
//
//
//
//            Row(
//                modifier = Modifier.fillMaxHeight(0.90f),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.dislike_book_icon),
//                    contentDescription = "dislike book icon",
//                    modifier = Modifier
//                        .size(100.dp)
//                        .zIndex(3f)
//                        .padding(end = 10.dp),
//                    colorFilter = ColorFilter.tint(Color.Black)
//
//                )
//                Card(
//                    backgroundColor = Color.Transparent,
//                    elevation = 5.dp,
//                    shape = RoundedCornerShape(20.dp),
//                    modifier = Modifier
//                        .fillMaxWidth(0.85f)
//                        .fillMaxHeight(0.99f)
//                        .draggableStack(
//                            controller = bookCardController,
//                            thresholdConfig = thresholdConfig,
//                        )
//                        .moveTo(
//                            x = if (index == i) bookCardController.offsetX.value else 0f,
//                            y = if (index == i) bookCardController.offsetY.value else 0f
//                        )
//                        .graphicsLayer(
//                            rotationZ = if (index == i) bookCardController.rotation.value else 0f,
//                        )
//                        .clickable {
//                            onClick(
//                                DataClickMetric(
//                                    Buttons.BOOK_CARD, Screens.Recommendation.route
//                                )
//                            )
//                            viewModel.isBookInfoDisplay.value = true
//                        }
//                        .alpha(alpha)
//
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp))
//                    ) {
//                        BookCardImage(
//                            uri = item.bookInfo.image,
//                            limitSwipeValue = limitSwipeValue,
//                            counter = if (index == viewModel.i - 1 || index == viewModel.i) {
//                                viewModel.counter
//                            } else 0,
//                            viewModel = viewModel,
//                            onClick = {
//                                onClick(
//                                    it
//                                )
//                                sessionTimer()
//                            },
//                            onNavigateToStatistics = onNavigateToStatistics
//                        )
//
////                    Column(
////                        modifier = Modifier
////                            .fillMaxSize()
////                            .padding(top = 30.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
////                        verticalArrangement = Arrangement.SpaceBetween
////                    ) {
////                        Row(
////                            modifier = Modifier.fillMaxWidth(),
////                            horizontalArrangement = Arrangement.SpaceBetween
////                        ) {
////                            Column(
////                                verticalArrangement = Arrangement.SpaceAround,
////                                modifier = Modifier.fillMaxWidth(0.7f)
////                            ) {
////                                Text(
////                                    text = item.bookInfo.title,
////                                    color = Color.White,
////                                    fontSize = 19.sp,
////                                    fontFamily = FontFamily(
////                                        Font(
////                                            R.font.manrope_extrabold, weight = FontWeight.W600
////                                        )
////                                    ),
////                                    maxLines = 1,
////                                    overflow = TextOverflow.Ellipsis
////                                )
////                                Spacer(modifier = Modifier.size(5.dp))
////                                Text(
////                                    text = item.bookInfo.author,
////                                    color = Color(0xFFA3A3A3),
////                                    fontSize = 16.sp,
////                                    fontFamily = FontFamily(
////                                        Font(
////                                            R.font.manrope_medium, weight = FontWeight.W600
////                                        )
////                                    )
////                                )
////                                Spacer(modifier = Modifier.size(5.dp))
////                                Text(
////                                    modifier = Modifier,
////                                    text = item.bookInfo.genre,
////                                    color = Color(0xFFFFFFFF),
////                                    fontSize = 14.sp,
////                                    fontFamily = FontFamily(
////                                        Font(
////                                            R.font.manrope_medium, weight = FontWeight.W600
////                                        )
////                                    )
////                                )
////
////                            }
////                            Image(
////                                painter = painterResource(id = R.drawable.igra_slov_logo),
////                                contentDescription = "Logo image",
////                                modifier = Modifier
////                                    .size(70.dp)
////                                    .clip(CircleShape)
////                            )
////                        }
////                        Box(
////                            modifier = Modifier
////                                .fillMaxWidth()
////                                .clip(RoundedCornerShape(50.dp))
////                                .background(Color(0xFF8BB298))
////                                .clickable {
////                                    onClick(
////                                        DataClickMetric(
////                                            Buttons.BUY_BOOK, Screens.Recommendation.route
////                                        )
////                                    )
////                                    val sendIntent: Intent = Intent(
////                                        Intent.ACTION_VIEW, Uri.parse(
////                                            item.buyUri
////                                        )
////                                    )
////                                    val webIntent = Intent.createChooser(sendIntent, null)
////                                    context.startActivity(webIntent)
////                                },
////                            contentAlignment = Alignment.Center
////                        ) {
////                            Text(
////                                modifier = Modifier.padding(10.dp), text = "Купить", color = Color(
////                                    0xFFFFFFFF
////                                )
////                            )
////                        }
////                    }
//
//                    }
//
////                    Box(
////                        modifier = Modifier
////                            .zIndex(2f)
////                            .background(brush),
////                        contentAlignment = Alignment.Center
////                    ) {
////                        when (item.onSwipeDirection) {
////                            DIRECTION_TOP -> {
////                                Box(
////                                    modifier = Modifier
////                                        .fillMaxSize()
////                                        .padding(bottom = 90.dp),
////                                    contentAlignment = Alignment.BottomCenter
////                                ) {
////                                    Box(
////                                        modifier = Modifier
////                                            .wrapContentSize()
////                                            .clip(RoundedCornerShape(10.dp))
////                                            .background(Color.White),
////                                        contentAlignment = Alignment.BottomCenter
////                                    ) {
////                                        Text(
////                                            text = "WANT IT",
////                                            fontSize = 28.sp,
////                                            fontWeight = FontWeight.Bold,
////                                            fontFamily = Manrope,
////                                            color = Color(0xFFE39C64),
////                                            modifier = Modifier
////                                                .padding(horizontal = 20.dp, vertical = 6.dp)
////                                        )
////                                    }
////                                }
////
////                            }
////
////                            DIRECTION_BOTTOM -> {
////                                Box(
////                                    modifier = Modifier
////                                        .fillMaxSize()
////                                        .padding(top = 90.dp),
////                                    contentAlignment = Alignment.TopCenter
////                                ) {
////                                    Box(
////                                        modifier = Modifier
////                                            .wrapContentSize()
////                                            .clip(RoundedCornerShape(10.dp))
////                                            .background(Color.Black),
////                                        contentAlignment = Alignment.TopCenter
////                                    ) {
////                                        Text(
////                                            text = "SKIP",
////                                            fontSize = 28.sp,
////                                            fontWeight = FontWeight.Bold,
////                                            fontFamily = Manrope,
////                                            color = Color.White,
////                                            modifier = Modifier
////                                                .padding(horizontal = 20.dp, vertical = 6.dp)
////                                        )
////                                    }
////                                }
////                            }
////
////                            DIRECTION_RIGHT -> {
////                                Box(
////                                    modifier = Modifier
////                                        .fillMaxSize()
////                                        .padding(start = 40.dp),
////                                    contentAlignment = Alignment.CenterStart
////                                ) {
////                                    Box(
////                                        modifier = Modifier
////                                            .wrapContentSize()
////                                            .clip(RoundedCornerShape(10.dp))
////                                            .background(Color.Green),
////                                        contentAlignment = Alignment.TopCenter
////                                    ) {
////                                        Text(
////                                            text = "LIKE",
////                                            fontSize = 28.sp,
////                                            fontWeight = FontWeight.Bold,
////                                            fontFamily = Manrope,
////                                            color = Color.Black,
////                                            modifier = Modifier
////                                                .padding(horizontal = 20.dp, vertical = 6.dp)
////                                        )
////                                    }
////                                }
////
////                            }
////
////                            DIRECTION_LEFT -> {
////                                Box(
////                                    modifier = Modifier
////                                        .fillMaxSize()
////                                        .padding(end = 40.dp),
////                                    contentAlignment = Alignment.CenterEnd
////                                ) {
////                                    Box(
////                                        modifier = Modifier
////                                            .wrapContentSize()
////                                            .clip(RoundedCornerShape(10.dp))
////                                            .background(Color.Red),
////                                        contentAlignment = Alignment.TopCenter
////                                    ) {
////                                        Text(
////                                            text = "DISLIKE",
////                                            fontSize = 28.sp,
////                                            fontWeight = FontWeight.Bold,
////                                            fontFamily = Manrope,
////                                            color = Color.White,
////                                            modifier = Modifier
////                                                .padding(horizontal = 20.dp, vertical = 6.dp)
////                                        )
////                                    }
////                                }
////                            }
////                        }
////                    }
//                }
//                Image(
//                    painter = painterResource(id = R.drawable.like_book_icon),
//                    contentDescription = "like book icon",
//                    modifier = Modifier
//                        .zIndex(3f)
//                        .size(100.dp)
//                        .padding(start = 10.dp)
////                        .graphicsLayer(
////                            scaleX = bookCardController.likeIconSize.value,
////                        )
//                )
//            }
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight()
//                , contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    modifier = Modifier.size(100.dp),
//                    painter = painterResource(id = R.drawable.skip_book_icon),
//                    contentDescription = "skip book icon",
//                )
//            }
//
//        }




}


fun Modifier.moveTo(
    x: Float, y: Float
) = this.then(Modifier.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)

    layout(placeable.width, placeable.height) {
        placeable.placeRelative(x.roundToInt(), y.roundToInt())
    }
})

fun Modifier.visible(
    visible: Boolean = true
) = this.then(Modifier.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)

    if (visible) {
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    } else {
        layout(0, 0) {}
    }
})

@Composable
fun BookCardImage(
    uri: String,
    limitSwipeValue: Int,
    counter: Int,
    viewModel: CardStackViewModel,
    onClick: (clickMetric: DataClickMetric) -> Unit = {},
    sessionTimer: () -> Unit = {},
    onNavigateToStatistics: () -> Unit
) {

    val isNotificationDisplay = viewModel.isNotificationDisplay.collectAsState(initial = "")
    if (counter == limitSwipeValue && isNotificationDisplay.value == false) {
        viewModel.countEqualToLimit()
    }
    if (counter == limitSwipeValue + 1) {
        viewModel.isFirstNotification.value = false
    }
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
//        Row(
//            modifier = Modifier
//                .zIndex(1f)
//                .fillMaxSize(), horizontalArrangement = Arrangement.End
//        ) {
//            if (viewModel.isFirstNotification.value) {
//                NotificationMessage(Modifier.padding(top = 20.dp), counter,onClick = {
//                    onClick(
//                        DataClickMetric(
//                            Buttons.STATS_NOTIFICATION,
//                            Screens.Recommendation.route
//                        )
//                    )
//                    onNavigateToStatistics()
//                })
//                viewModel.countEqualToLimit()
//            }
//            Notification(
//                count = counter,
//                Modifier
////                    .align(Alignment.TopEnd)
//                    .padding(top = 100.dp)
//                    .zIndex(1f),
//                onClick = {
//                    onClick(
//                        DataClickMetric(
//                            Buttons.STATS_NOTIFICATION,
//                            Screens.Recommendation.route
//                        )
//                    )
//                    sessionTimer()
////                   navController.popBackStack()
//                    onNavigateToStatistics()
//                }
//            )
//        }

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
fun RecomendationIcon(
    bookCardController: BookCardController,
    modifier: Modifier,
    size: Dp,
    color: Color,
    icon: Int,
    alpha: Float,
    description: String
) {
    Box(modifier = modifier) {
        Box(contentAlignment = Alignment.Center){
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
                        .size(size+20.dp)
                        .alpha(alpha/3),
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

const val DIRECTION_LEFT = "direction_left"
const val DIRECTION_RIGHT = "direction_right"
const val DIRECTION_TOP = "direction_top"
const val DIRECTION_BOTTOM = "direction_bottom"