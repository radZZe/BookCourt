package com.example.bookcourt.utils

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.bookcourt.R
import com.example.bookcourt.models.Book
import com.example.bookcourt.models.BookInfo
import com.example.bookcourt.models.ClickMetric
import com.example.bookcourt.models.User
import com.example.bookcourt.ui.CardInfoScreen
import com.example.bookcourt.ui.recomendation.Notification
import com.example.bookcourt.ui.recomendation.NotificationMessage
import com.example.bookcourt.ui.theme.Gilroy
import com.example.bookcourt.ui.theme.Manrope
import com.example.bookcourt.ui.theme.CustomButton
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardStack(
    user: User,
    modifier: Modifier = Modifier,
    itemsRaw: List<Book>,
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) },
    velocityThreshold: Dp = 125.dp,
    onSwipeLeft: (item: Book) -> Unit = {},
    onSwipeRight: (item: Book) -> Unit = {},
    onSwipeUp: (item: Book) -> Unit = {},
    onSwipeDown: (item: Book) -> Unit = {},
    onClick: (clickMetric: ClickMetric) -> Unit = {},
    onEmptyStack: () -> Unit = {},
    sessionTimer: () -> Unit = {},
//    cardStackController: CardStackController,
    viewModel: CardStackViewModel = hiltViewModel(),
    navController: NavController
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
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(0.dp)
        ) {
            val stack = createRef()

            Box(
                modifier = modifier
                    .constrainAs(stack) {
                        top.linkTo(parent.top)
                    }
                    .fillMaxHeight()
            ) {
                items.forEachIndexed { index, item ->
                    BookCard(
                        user,
                        item,
                        viewModel,
                        limitSwipeValue,
                        index,
                        onSwipeLeft,
                        onSwipeRight,
                        onSwipeUp,
                        onSwipeDown,
                        { onClick(it) },
                        thresholdConfig,
                        sessionTimer,
                        navController
                    )
                }
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
//                viewModel.updateUserStatistic(user)
//                navController.popBackStack()
                navController.navigate(route = Screens.Statistics.route)
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookCard(
    user: User,
    itemRaw: MutableState<Book>,
    viewModel: CardStackViewModel,
    limitSwipeValue: Int,
    index: Int,
    onSwipeLeft: (item: Book) -> Unit = {},
    onSwipeRight: (item: Book) -> Unit = {},
    onSwipeUp: (item: Book) -> Unit = {},
    onSwipeDown: (item: Book) -> Unit = {},
    onClick: (clickMetric: ClickMetric) -> Unit = {},
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) },
    sessionTimer: () -> Unit = {},
    navController: NavController
) {

    var item = itemRaw.value
    var i = viewModel.i
    val cardStackController = rememberCardStackController()
    cardStackController.onSwipeLeft = {

        //viewModel.dislikeBook(item.genre)
        //viewModel.readBooks(item.name)
        //onSwipeLeft(item)

//        viewModel.readBooks.add(item)
        item.onSwipeDirection = DIRECTION_LEFT
        user.readBooksList.add(item)
        onSwipeLeft(item)
        viewModel.updateUserStatistic(user)

        viewModel.i--
        if (i != -1) viewModel.changeCurrentItem()
        viewModel.counter++
    }

    cardStackController.onSwipeRight = {

        // viewModel.likeBook(item.genre)
        //viewModel.readBooks(item.name)
        //onSwipeRight(item)

//        viewModel.readBooks.add(item)
        onSwipeRight(item)
        item.onSwipeDirection = DIRECTION_RIGHT
        user.readBooksList.add(item)
        viewModel.updateUserStatistic(user)
        viewModel.i--
        if (i != -1) viewModel.changeCurrentItem()
        viewModel.counter++
    }

    cardStackController.onSwipeUp = {

        //viewModel.wantToRead(item.name)
        //onSwipeUp(item)

//        viewModel.wantToRead.add(item)
        item.onSwipeDirection = DIRECTION_TOP
        user.wantToRead.add(item)
        viewModel.updateUserStatistic(user)
        onSwipeUp(item)
        viewModel.i--
        if (i != -1) viewModel.changeCurrentItem()
        viewModel.counter++
    }

    cardStackController.onSwipeDown = {

        onSwipeDown(item)
        viewModel.i--
        if (i != -1) viewModel.changeCurrentItem()
        viewModel.counter++
    }
    val context = LocalContext.current
    var colorStopsNull = arrayOf(
        0.0f to Color.Transparent, 0.8f to Color.Transparent
    )
    var brush = Brush.verticalGradient(colorStops = colorStopsNull)

    when (item.onSwipeDirection) {
        DIRECTION_RIGHT -> {
            val colorStopsRight = arrayOf(
                0.0f to Color(0.3f, 0.55f, 0.21f, 0.75f), 0.8f to Color.Transparent
            )
            brush = Brush.horizontalGradient(colorStops = colorStopsRight)
        }
        DIRECTION_LEFT -> {
            val colorStopsLeft = arrayOf(

                0.0f to Color.Transparent, 0.8f to Color(1f, 0.31f, 0.31f, 0.75f)
            )
            brush = Brush.horizontalGradient(colorStops = colorStopsLeft)
        }
        DIRECTION_TOP -> { // Note the block
            val colorStopsTop = arrayOf(
                0.0f to Color.Transparent,
                0.8f to Color(1f, 0.6f, 0f, 0.75f),
            )
            brush = Brush.verticalGradient(colorStops = colorStopsTop)
        }
        DIRECTION_BOTTOM -> {
            val colorStopsBottom = arrayOf(
                0.0f to Color(0.3f, 0f, 0.41f, 0.75f), 0.8f to Color.Transparent
            )

            brush = Brush.verticalGradient(colorStops = colorStopsBottom)
        }
        else -> {
            brush = Brush.verticalGradient(colorStops = colorStopsNull)
        }
    }

    val darkGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF303845), Color(0xFF2E3643), Color(0xFF14161A)
        )
    )

    if (viewModel.isBookInfoDisplay.value) {
        var book = BookInfo(
            title = item.bookInfo.title,
            author = item.bookInfo.author,
            description = item.bookInfo.description,
            numberOfPages = item.bookInfo.numberOfPages,
            rate = item.bookInfo.rate,
            genre = item.bookInfo.genre,
            price = item.bookInfo.price,
            image = item.bookInfo.image
        )
        CardInfoScreen(
            book = book,
            onClick = {
                onClick(
                    ClickMetric(
                        Buttons.BOOK_CARD, Screens.Recommendation.route
                    )
                )
                viewModel.isBookInfoDisplay.value = false
            },
            modifier = Modifier.visible(visible = index == i)
        )
    } else {
        Card(
            backgroundColor = Color.Transparent,
            elevation = 5.dp,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .draggableStack(
                    controller = cardStackController,
                    thresholdConfig = thresholdConfig,
                )
                .moveTo(
                    x = if (index == i) cardStackController.offsetX.value else 0f,
                    y = if (index == i) cardStackController.offsetY.value else 0f
                )
                .visible(visible = index == i || index == i - 1)
                .graphicsLayer(
                    rotationZ = if (index == i) cardStackController.rotation.value else 0f,
                )
                .clickable {
//                    navController.navigate(
//                        Screens.CardInfo.route +
//                                "/${item.name}/${item.author}/${item.description}/${item.genre}"
//                                + "/${item.numberOfPage}/${item.rate}/${item.price}"
//                    )
                    onClick(
                        ClickMetric(
                            Buttons.BOOK_CARD, Screens.Recommendation.route
                        )
                    )
                    viewModel.isBookInfoDisplay.value = true
                }
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(darkGradient)
                    .clip(RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush)
                ) {
                    Box(modifier = Modifier.wrapContentSize()) {
                        BookCardImage(
                            uri = item.bookInfo.image,
                            limitSwipeValue = limitSwipeValue,
                            counter = if (index == viewModel.i - 1 || index == viewModel.i) {
                                viewModel.counter
                            } else 0,
                            viewModel = viewModel,
//                            counter = viewModel.isNotificationDisplay.value,
//                            onClick = onClick(ClickMetric(
//                                Buttons.STATS_NOTIFICATION, BottomBarScreen.Recomendations.route
//                            )),
                            onClick = {
                                onClick(
                                    ClickMetric(
                                        Buttons.STATS_NOTIFICATION, Screens.Recommendation.route
                                    )
                                )
                                sessionTimer()
                            },
                            navController = navController
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 30.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier.fillMaxWidth(0.7f)
                            ) {
                                Text(
                                    text = item.bookInfo.title,
                                    color = Color.White,
                                    fontSize = 19.sp,
                                    fontFamily = FontFamily(
                                        Font(
                                            R.font.manrope_extrabold, weight = FontWeight.W600
                                        )
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.size(5.dp))
                                Text(
                                    text = item.bookInfo.author,
                                    color = Color(0xFFA3A3A3),
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(
                                        Font(
                                            R.font.manrope_medium, weight = FontWeight.W600
                                        )
                                    )
                                )
                                Spacer(modifier = Modifier.size(5.dp))
                                Text(
                                    modifier = Modifier,
                                    text = item.bookInfo.genre,
                                    color = Color(0xFFFFFFFF),
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(
                                        Font(
                                            R.font.manrope_medium, weight = FontWeight.W600
                                        )
                                    )
                                )

                            }
                            Image(
                                painter = painterResource(id = R.drawable.igra_slov_logo),
                                contentDescription = "Logo image",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFF8BB298))
                                .clickable {
                                    onClick(
                                        ClickMetric(
                                            Buttons.BUY_BOOK, Screens.Recommendation.route
                                        )
                                    )
                                    val sendIntent: Intent = Intent(
                                        Intent.ACTION_VIEW, Uri.parse(
                                            item.buyUri
                                        )
                                    )
                                    val webIntent = Intent.createChooser(sendIntent, null)
                                    context.startActivity(webIntent)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(10.dp), text = "Купить", color = Color(
                                    0xFFFFFFFF
                                )
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .background(brush),
                contentAlignment = Alignment.Center
            ) {
                when (item.onSwipeDirection) {
                    DIRECTION_TOP -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 90.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Text(
                                    text = "WANT IT",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Manrope,
                                    color = Color(0xFFE39C64),
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp, vertical = 6.dp)
                                )
                            }
                        }

                    }

                    DIRECTION_BOTTOM -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 90.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Black),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Text(
                                    text = "SKIP",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Manrope,
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    DIRECTION_RIGHT -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 40.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Green),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Text(
                                    text = "LIKE",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Manrope,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp, vertical = 6.dp)
                                )
                            }
                        }

                    }

                    DIRECTION_LEFT -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(end = 40.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Red),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Text(
                                    text = "DISLIKE",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Manrope,
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
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
    counter:Int,
    viewModel: CardStackViewModel,
    onClick: (clickMetric: ClickMetric) -> Unit = {},
    sessionTimer: () -> Unit = {},
    navController: NavController
) {

    val isNotificationDisplay = viewModel.isNotificationDisplay.collectAsState(initial = "")
    if (counter == limitSwipeValue && isNotificationDisplay.value == false) {
        viewModel.countEqualToLimit()
    }
    if(counter==limitSwipeValue+1){
        viewModel.isFirstNotification.value = false
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f), contentAlignment = Alignment.Center
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
        Row(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxSize(), horizontalArrangement = Arrangement.End
        ) {
            if (viewModel.isFirstNotification.value) {
                NotificationMessage(Modifier.padding(top = 20.dp), counter,onClick = {
//                    navController.popBackStack()
                    navController.navigate(route = Screens.Statistics.route)
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
                    onClick(
                        ClickMetric(
                            Buttons.STATS_NOTIFICATION,
                            Screens.Recommendation.route
                        )
                    )
                    sessionTimer()
//                    navController.popBackStack()
                    navController.navigate(route = Screens.Statistics.route)
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

const val DIRECTION_LEFT = "direction_left"
const val DIRECTION_RIGHT = "direction_right"
const val DIRECTION_TOP = "direction_top"
const val DIRECTION_BOTTOM = "direction_bottom"