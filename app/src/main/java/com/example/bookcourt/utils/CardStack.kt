package com.example.bookcourt.utils

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
    onEmptyStack: (lastItem: Book) -> Unit = {},
    cardStackController: CardStackController,
    viewModel: CardStackViewModel = hiltViewModel(),
    navController: NavController
) {
    viewModel.allBooks.value = items
    var allBooks = viewModel.allBooks.value
    var i by remember {
        mutableStateOf(allBooks.size - 1)
    }
    if (i != -1) viewModel.currentItem.value = allBooks[i]

    if (i == -1) {
        onEmptyStack(allBooks.last())
    }


    cardStackController.onSwipeLeft = {
        viewModel.dislikeBook(allBooks[i].genre)
        onSwipeLeft(allBooks[i])
        i--
        if (i != -1) viewModel.changeCurrentItem(allBooks[i])
    }

    cardStackController.onSwipeRight = {
        viewModel.likeBook(allBooks[i].genre)
        onSwipeRight(allBooks[i])
        i--
        if (i != -1) viewModel.changeCurrentItem(allBooks[i])
    }

    cardStackController.onSwipeUp = {
        viewModel.wantToRead(allBooks[i].name)
        onSwipeUp(allBooks[i])
        i--
        if (i != -1) viewModel.changeCurrentItem(allBooks[i])
    }

    cardStackController.onSwipeDown = {
        onSwipeDown(allBooks[i])
        i--
        if (i != -1) viewModel.changeCurrentItem(allBooks[i])
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
            allBooks.forEachIndexed { index, item ->
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
                                        "/${item.name}/${item.author}/${item.description}/${item.genre}"
                                        + "/${item.createdAt}/${item.numberOfPage}/${item.rate}"
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
fun BookCard1(
    modifier: Modifier = Modifier,
    item: Book,
    viewModel: CardStackViewModel
) {
    var colorStopsNull = arrayOf(
        0.0f to Color.Transparent,
        0.8f to Color.Transparent
    )
    var brush = Brush.verticalGradient(colorStops = colorStopsNull)
    when (item.onSwipeDirection.value) {
        DIRECTION_RIGHT -> {
            val colorStopsRight = arrayOf(
                0.0f to Color(0.3f, 0.55f, 0.21f, 0.75f),
                0.8f to Color.Transparent
            )
            brush = Brush.horizontalGradient(colorStops = colorStopsRight)
        }
        DIRECTION_LEFT -> {
            val colorStopsLeft = arrayOf(

                0.0f to Color.Transparent,
                0.8f to Color(1f, 0.31f, 0.31f, 0.75f)
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
                0.0f to Color(0.3f, 0f, 0.41f, 0.75f),
                0.8f to Color.Transparent
            )

            brush = Brush.verticalGradient(colorStops = colorStopsBottom)
        }
        else -> {
            brush = Brush.verticalGradient(colorStops = colorStopsNull)
        }
    }





    Card(
        backgroundColor = Color.Transparent,
        elevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(modifier = Modifier.background(Color.White)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush)
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
                    Text(
                        text = stringResource(id = R.string.book_rating_info, item.rate),
                        color = colorResource(id = R.color.rating_color),
                        fontSize = 16.sp
                    )
                }

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

    var colorStopsNull = arrayOf(
        0.0f to Color.Transparent,
        0.8f to Color.Transparent
    )
    var brush = Brush.verticalGradient(colorStops = colorStopsNull)
    when (item.onSwipeDirection.value) {
        DIRECTION_RIGHT -> {
            val colorStopsRight = arrayOf(
                0.0f to Color(0.3f, 0.55f, 0.21f, 0.75f),
                0.8f to Color.Transparent
            )
            brush = Brush.horizontalGradient(colorStops = colorStopsRight)
        }
        DIRECTION_LEFT -> {
            val colorStopsLeft = arrayOf(

                0.0f to Color.Transparent,
                0.8f to Color(1f, 0.31f, 0.31f, 0.75f)
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
                0.0f to Color(0.3f, 0f, 0.41f, 0.75f),
                0.8f to Color.Transparent
            )

            brush = Brush.verticalGradient(colorStops = colorStopsBottom)
        }
        else -> {
            brush = Brush.verticalGradient(colorStops = colorStopsNull)
        }
    }

    val darkGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF303845),
            Color(0xFF2E3643),
            Color(0xFF14161A)
        )
    )

    Card(
        backgroundColor = Color.Transparent,
        elevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxSize()
    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(darkGradient)
                .clip(RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp))
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(brush)) {
                Box(modifier = Modifier.wrapContentSize()) {
                    BookCardImage(uri = item.image)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 60.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                    ){
                    Text(text="48 законов власти",color = Color.White, fontSize = 20.sp,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.manrope_extrabold,
                                    weight = FontWeight.W600
                                )
                            ))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text="Грин Р.", color = Color(0xFFFFFDFF), fontSize = 16.sp,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.manrope_medium,
                                    weight = FontWeight.W600
                                )
                            ))
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxSize()){
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(color = Color(0xFF8BB298))
                                .padding(),
                            Alignment.Center,

                            ) {
                            Text(
                                modifier = Modifier.padding(start = 10.dp, end=10.dp, top = 4.dp, bottom = 4.dp),
                                text = "Фантастика", color = Color(0xFFFFFFFF),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.manrope_medium,
                                        weight = FontWeight.W600
                                    )
                                )
                            )
                        }
                    }
                }
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(start = 20.dp, bottom = 0.dp),
//                    contentAlignment = Alignment.BottomStart
//                ) {
//                    Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
//                        Text(
//                            text = item.name, color = Color.White, fontSize = 20.sp,
//                            fontFamily = FontFamily(
//                                Font(
//                                    R.font.manrope_extrabold,
//                                    weight = FontWeight.W600
//                                )
//                            )
//                        )
//                        Text(
//                            text = item.author, color = Color(0xFFFFFDFF), fontSize = 16.sp,
//                            fontFamily = FontFamily(
//                                Font(
//                                    R.font.manrope_medium,
//                                    weight = FontWeight.W600
//                                )
//                            )
//                        )
//                    }
//                }
//                Row(
//                    modifier = Modifier,
//                    horizontalArrangement = Arrangement.spacedBy(10.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .padding( start = 10.dp, bottom = 15.dp)
//                            .fillMaxSize(),
//                        verticalArrangement = Arrangement.Bottom
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .size(width = 132.dp, height = 32.dp)
//                                .clip(RoundedCornerShape(50.dp))
//                                .background(color = Color(0xFF8BB298))
//                                .clickable { },
//                            Alignment.Center,
//
//
//                            ) {
//                            Text(text = item.genre, color = Color(0xFFFFFFFF), fontSize = 14.sp)
//                        }
//
//
//                    }
//                }
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(
//                            top = 810.dp, bottom = 5.dp, start = 350.dp, end = 5.dp
//                        )
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.shop),
//                        contentDescription = "icon_shop",
//                        modifier = Modifier
//                            .size(width = 25.dp, height = 24.dp)
//                    )
//                }
            }


        }
        Box(modifier = Modifier.zIndex(2f).background(brush))
    }

}


@Composable
fun BookCardTest(
    modifier: Modifier = Modifier,

    ) {
    var item = Book(
        name = "test",
        author = "test",
        description = "test",
        createdAt = "test",
        numberOfPage = "test",
        rate = 1,
        owner = "test",
        genre = "test",
        image = "https://cv6.litres.ru/pub/c/elektronnaya-kniga/cover_415/36628165-ray-dalio-principy-zhizn-i-rabota.webp",
        onSwipeDirection = remember {
            mutableStateOf(DIRECTION_TOP)
        }
    )
    var colorStopsNull = arrayOf(
        0.0f to Color.Transparent,
        0.8f to Color.Transparent
    )
    var brush = Brush.verticalGradient(colorStops = colorStopsNull)
    when (item.onSwipeDirection.value) {
        DIRECTION_RIGHT -> {
            val colorStopsRight = arrayOf(
                0.0f to Color(0.3f, 0.55f, 0.21f, 0.75f),
                0.8f to Color.Transparent
            )
            brush = Brush.horizontalGradient(colorStops = colorStopsRight)
        }
        DIRECTION_LEFT -> {
            val colorStopsLeft = arrayOf(
                0.0f to Color(1f, 0.31f, 0.31f, 0.75f),
                0.8f to Color.Transparent
            )
            brush = Brush.horizontalGradient(colorStops = colorStopsLeft)
        }
        DIRECTION_TOP -> { // Note the block
            val colorStopsTop = arrayOf(
                0.1f to Color(1f, 0.6f, 0f, 0.75f),
                0.8f to Color.Transparent
            )
            brush = Brush.verticalGradient(colorStops = colorStopsTop)
        }
        DIRECTION_BOTTOM -> {
            val colorStopsBottom = arrayOf(
                0.0f to Color(0.3f, 0f, 0.41f, 0.75f),
                0.1f to Color.Transparent
            )

            brush = Brush.verticalGradient(colorStops = colorStopsBottom)
        }
        else -> {
            brush = Brush.verticalGradient(colorStops = colorStopsNull)
        }
    }
    val listColors = listOf(Color(0.3f, 0f, 0.41f, 0.75f), Color.Transparent)
    val customBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0.3f, 0f, 0.41f, 0.75f),
            0.5f to Color.Transparent
        )
    )
    // Убери старт Y и енд Y
//    val customBrush = remember {
//        object : ShaderBrush() {
//            override fun createShader(size: Size): Shader {
//                return LinearGradientShader(
//                    colors = listColors,
//                    colorStops = listOf(0.1f,0.8f),
//                    from = Offset.Zero,
//                    to = Offset(size.width / 3f, 0f),
//                )
//            }
//        }
//    }


    Card(
        backgroundColor = Color.Transparent,
        elevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(modifier = Modifier.background(Color.White)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(customBrush)
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
                    Text(
                        text = stringResource(id = R.string.book_rating_info, item.rate),
                        color = colorResource(id = R.color.rating_color),
                        fontSize = 16.sp
                    )
                }
            }

        }
    }

}

@Preview
@Composable
fun BookCardTestPreview() {
    BookCardTest(modifier = Modifier)
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


const val DIRECTION_LEFT = "direction_left"
const val DIRECTION_RIGHT = "direction_right"
const val DIRECTION_TOP = "direction_top"
const val DIRECTION_BOTTOM = "direction_bottom"