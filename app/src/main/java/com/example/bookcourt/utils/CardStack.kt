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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.bookcourt.R
import com.example.bookcourt.models.Book
import com.example.bookcourt.ui.BookCardImage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardStack(
    modifier: Modifier = Modifier,
    items: List<Book>,
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) },
    velocityThreshold: Dp = 125.dp,
    onSwipeLeft: (item: Book) -> Unit = {},
    onSwipeRight: (item: Book) -> Unit = {},
    onSwipeUp:(item:Book) ->Unit = {},
    onSwipeDown:(item:Book)->Unit ={},
    onEmptyStack: (lastItem: Book) -> Unit = {},
    cardStackController: CardStackController,
    viewModel: CardStackViewModel = hiltViewModel(),
    navController: NavController
) {
    var i by remember {
        mutableStateOf(items.size - 1)
    }

    if (i == -1) {
        onEmptyStack(items.last())
    }


    cardStackController.onSwipeLeft = {
       viewModel.dislikeBook(items[i].genre)
        onSwipeLeft(items[i])
        i--
    }

    cardStackController.onSwipeRight = {
        viewModel.likeBook(items[i].genre)
        onSwipeRight(items[i])
        i--
    }

    cardStackController.onSwipeUp = {
        viewModel.wantToRead(items[i].name)
        onSwipeUp(items[i])
        i--
    }

    cardStackController.onSwipeDown = {
        onSwipeDown(items[i])
        i--
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
                .draggableStack(
                    controller = cardStackController,
                    thresholdConfig = thresholdConfig,
                )
                .fillMaxHeight()
        ) {
            items.asReversed().forEachIndexed { index, item ->
                BookCard(
                    modifier = Modifier
                        .moveTo(
                            x = if (index == i) cardStackController.offsetX.value else 0f,
                            y = if (index == i) cardStackController.offsetY.value else 0f
                        )
                        .visible(visible = index == i || index == i - 1)
                        .clickable {
                            navController.navigate(
                                Screens.CardInfo.route +
                                        "/${item.name}/${item.author}/${item.description}/${item.genre}"
                                        + "/${item.createdAt}/${item.numberOfPage}/${item.rate}"
                            )
                        },
                    item,
                    navController
                )
            }

        }
    }


}

@Composable
fun BookCard1(
    modifier: Modifier = Modifier,
    item: Book,
    navController: NavController
) {
    Card(
        elevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        Box {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                BookCardImage(uri = item.image)
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = item.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.author,
                            color = colorResource(id = R.color.bottom_nav_bg),
                            fontSize = 16.sp
                        )
                        Text(
                            text = item.genre,
                            color = colorResource(id = R.color.bottom_nav_bg),
                            fontSize = 16.sp
                        )
                        Text(
                            text = item.createdAt,
                            color = colorResource(id = R.color.bottom_nav_bg),
                            fontSize = 16.sp
                        )
                        Text(
                            text = "${item.numberOfPage} стр",
                            color = colorResource(id = R.color.bottom_nav_bg),
                            fontSize = 16.sp
                        )
                    }
                    Text(text = item.description, maxLines = 3, overflow = TextOverflow.Ellipsis)
                    Text(text =  stringResource(id = R.string.book_rating_info,item.rate), color = colorResource(id = R.color.rating_color),
                        fontSize = 16.sp)
                }

            }

        }
    }
}

@Composable
fun BookCard(modifier: Modifier = Modifier,
                  item: Book,
                  navController: NavController) {
    Card(
        elevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxSize()
    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF303845),
                            Color(0xFF2E3643),
                            Color(0xFF14161A)
                        )
                    )
                )
                .clip(RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp))
        ) {
            BookCardImage(uri = item.image)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, bottom = 60.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = item.name, color = Color.White, fontSize = 20.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.manrope_extrabold,
                                weight = FontWeight.W600
                            )
                        )
                    )
                    Text(
                        text = item.author, color = Color(0xFFFFFDFF), fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.manrope_medium,
                                weight = FontWeight.W600
                            )
                        )
                    )
                }
            }
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 800.dp, start = 10.dp, bottom = 15.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 132.dp, height = 32.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(color = Color(0xFF8BB298))
                            .clickable { },
                        Alignment.Center,


                        ) {
                        Text(text = item.genre, color = Color(0xFFFFFFFF), fontSize = 14.sp)
                    }


                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 810.dp, bottom = 5.dp, start = 350.dp, end = 5.dp
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.shop),
                    contentDescription = "icon_shop",
                    modifier = Modifier
                        .size(width = 25.dp, height = 24.dp)
                )
            }

        }
    }
}

fun Modifier.moveTo(
    x: Float,
    y: Float
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