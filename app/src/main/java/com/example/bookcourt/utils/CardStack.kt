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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.*
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
import com.example.bookcourt.R
import com.example.bookcourt.models.Book
import com.example.bookcourt.ui.BookCardImage
import com.example.bookcourt.ui.theme.Gilroy
import com.example.bookcourt.ui.theme.Manrope
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardStack(
    modifier: Modifier = Modifier,
    items: List<Book>,
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) },
    velocityThreshold: Dp = 125.dp,
    onSwipeLeft: (item: Book) -> Unit = {},
    onSwipeRight: (item: Book) -> Unit = {},
    onSwipeUp: (item: Book) -> Unit = {},
    onSwipeDown: (item: Book) -> Unit = {},
    onEmptyStack: () -> Unit = {},
    cardStackController: CardStackController,
    viewModel: CardStackViewModel = hiltViewModel(),
    navController: NavController
) {
//    val readBooksList = viewModel.readBooks.collectAsState(initial = "")
    var i by remember {
        mutableStateOf(items.size - 1)
    }

    if (i != -1) viewModel.currentItem.value = items[i]

    if (i == -1) {
        onEmptyStack()
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getReadBooks()
    }

    cardStackController.onSwipeLeft = {

//        viewModel.dislikeBook(items[i].genre)
//        viewModel.readBooks(items[i].name)

//        viewModel.readBooks.value!!.add(items[i])
        viewModel.readBooks.add(items[i])
        onSwipeLeft(items[i])
        viewModel.updateUserStatistic()
        i--
        if (i != -1) viewModel.changeCurrentItem(items[i])
    }

    cardStackController.onSwipeRight = {

//        viewModel.likeBook(items[i].genre)
//        viewModel.readBooks(items[i].name)

//        viewModel.readBooks.value!!.add(items[i])
        viewModel.readBooks.add(items[i])
        onSwipeRight(items[i])
        viewModel.updateUserStatistic()
        i--
        if (i != -1) viewModel.changeCurrentItem(items[i])
    }

    cardStackController.onSwipeUp = {

//        viewModel.wantToRead(items[i].name)

//        viewModel.wantToRead.value!!.add(items[i])
        viewModel.wantToRead.add(items[i])
        viewModel.updateUserStatistic()
        onSwipeUp(items[i])
        i--
        if (i != -1) viewModel.changeCurrentItem(items[i])
    }

    cardStackController.onSwipeDown = {
        onSwipeDown(items[i])
        i--
        if (i != -1) viewModel.changeCurrentItem(items[i])
    }
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
                                navController.navigate(
                                    Screens.CardInfo.route +
                                            "/${item.bookInfo.title}/${item.bookInfo.author}/${item.bookInfo.description}/${item.bookInfo.genre}"
                                            + "/${item.bookInfo.numberOfPages}/${item.bookInfo.rate}/${item.bookInfo.price}"
                                )
                            },
                        item,
                        navController,
                        viewModel
                    )
            }

        }
    }


}


@Composable
fun BookCard(
    modifier: Modifier = Modifier,
    item: Book,
    navController: NavController,
    viewModel: CardStackViewModel
) {

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

    Card(
        backgroundColor = Color.Transparent,
        elevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.fillMaxSize()
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
                    BookCardImage(uri = item.bookInfo.image)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.bookInfo.title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.manrope_extrabold, weight = FontWeight.W600
                            )
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.bookInfo.author,
                        color = Color(0xFFFFFDFF),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.manrope_medium, weight = FontWeight.W600
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(color = Color(0xFF8BB298))
                                .padding(),
                            Alignment.Center,

                            ) {
                            Text(
                                modifier = Modifier.padding(
                                    start = 10.dp, end = 10.dp, top = 4.dp, bottom = 4.dp
                                ),
                                text = item.bookInfo.genre,
                                color = Color(0xFFFFFFFF),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.manrope_medium, weight = FontWeight.W600
                                    )
                                )
                            )

                        }
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFF483936))
                            .clickable {
                                val sendIntent: Intent = Intent(
                                    Intent.ACTION_VIEW, Uri.parse(
                                        item.buyUri
                                    )
                                )
                                val webIntent = Intent.createChooser(sendIntent, null)
                                context.startActivity(webIntent)
                            }) {
                            Text(
                                modifier = Modifier.padding(10.dp), text = "Купить", color = Color(
                                    0xFFB2AC8B
                                )
                            )
                        }
                    }
                }
            }


        }

        Box(modifier = Modifier
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
                else -> {}
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

const val DIRECTION_LEFT = "direction_left"
const val DIRECTION_RIGHT = "direction_right"
const val DIRECTION_TOP = "direction_top"
const val DIRECTION_BOTTOM = "direction_bottom"