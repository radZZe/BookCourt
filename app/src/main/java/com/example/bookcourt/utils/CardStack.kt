package com.example.bookcourt.utils

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.bookcourt.BookCardImage
import com.example.bookcourt.models.Book
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardStack(
    modifier: Modifier = Modifier,
    items: MutableList<Book>,
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f)},
    velocityThreshold: Dp = 125.dp,
    onSwipeLeft: (item: Book) -> Unit = {},
    onSwipeRight: (item: Book) -> Unit = {},
    onEmptyStack: (lastItem: Book) -> Unit = {}
) {
    var i by remember {
        mutableStateOf(items.size - 1)
    }

    if (i == -1) {
        onEmptyStack(items.last())
    }

    val cardStackController = rememberCardStackController()

    cardStackController.onSwipeLeft = {
        onSwipeLeft(items[i])
        i--
    }

    cardStackController.onSwipeRight = {
        onSwipeRight(items[i])
        i--
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
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
                    velocityThreshold = velocityThreshold
                )
                .fillMaxHeight()
        ) {
            items.asReversed().forEachIndexed { index, item ->
                BookCard(
                    modifier = Modifier.moveTo(
                        x = if (index == i) cardStackController.offsetX.value else 0f,
                        y = if (index == i) cardStackController.offsetY.value else 0f
                    )
                        .visible(visible = index == i || index == i -1)
                        .graphicsLayer(
                            rotationZ = if (index == i) cardStackController.rotation.value else 0f,
                            scaleX = if (index < i) cardStackController.scale.value else 1f,
                            scaleY = if (index < i) cardStackController.scale.value else 1f
                        ),
                    item,
                    cardStackController
                )
            }
        }
    }
}

//@Composable
//fun Card(
//    modifier: Modifier = Modifier,
//    item: Book,
//    cardStackController: CardStackController
//) {
//    Box(modifier = modifier) {
//        if (item.url != null) {
//            AsyncImage(
//                model = item.url,
//                contentDescription = "",
//                contentScale = ContentScale.Crop,
//                modifier = modifier.fillMaxSize()
//            )
//        }
//
//        Column(
//            modifier = modifier
//                .align(Alignment.BottomStart)
//                .padding(10.dp)
//        ) {
//            Text(text = item.text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 25.sp)
//
//            Text(text = item.subText, color = Color.White, fontSize = 20.sp)
//
//            Row {
//                IconButton(
//                    modifier = modifier.padding(50.dp, 0.dp, 0.dp, 0.dp),
//                    onClick = { cardStackController.swipeLeft() },
//                ) {
//                    Icon(
//                        Icons.Default.Close, contentDescription = "", tint = Color.White, modifier =
//                        modifier
//                            .height(50.dp)
//                            .width(50.dp)
//                    )
//                }
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                IconButton(
//                    modifier = modifier.padding(0.dp, 0.dp, 50.dp, 0.dp),
//                    onClick = { cardStackController.swipeRight() }
//                ) {
//                    Icon(
//                        Icons.Default.FavoriteBorder, contentDescription = "", tint = Color.White, modifier =
//                        modifier.height(50.dp).width(50.dp)
//                    )
//                }
//            }
//        }
//    }
//}


@Composable
fun BookCard(
    modifier: Modifier = Modifier,
    item: Book,
    cardStackController: CardStackController
) {
    Card(
        elevation = 10.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
    ) {
        Box(){
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                BookCardImage(uri = item.image)
                Text(text = item.name, fontWeight = FontWeight.Bold)
                Row() {
                    Text(text = item.author, color = Color.Black)
                    Text(text = item.createdAt)
                    Text(text = item.numberOfPage)
                }
                Text(text = item.description)
            }
            Row {
                IconButton(
                    modifier = modifier.padding(50.dp, 0.dp, 0.dp, 0.dp),
                    onClick = { cardStackController.swipeLeft() },
                ) {
                    Icon(
                        Icons.Default.Close, contentDescription = "", tint = Color.White, modifier =
                        modifier
                            .height(50.dp)
                            .width(50.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    modifier = modifier.padding(0.dp, 0.dp, 50.dp, 0.dp),
                    onClick = { cardStackController.swipeRight() }
                ) {
                    Icon(
                        Icons.Default.FavoriteBorder, contentDescription = "", tint = Color.White, modifier =
                        modifier.height(50.dp).width(50.dp)
                    )
                }
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
) = this.then(Modifier.layout{ measurable, constraints ->
    val placeable = measurable.measure(constraints)

    if (visible) {
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    } else {
        layout(0, 0) {}
    }
})