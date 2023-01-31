package com.example.bookcourt.utils

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.models.Book
import com.example.bookcourt.ui.BookCardImage
import com.example.bookcourt.ui.profile.ProfileViewModel
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
    viewModel: CardStackViewModel = hiltViewModel()
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
                        .visible(visible = index == i || index == i - 1),
                    item,
                )
            }

        }
    }


}


@Composable
fun BookCard(
    modifier: Modifier = Modifier,
    item: Book,
) {
    Card(
        elevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        Box() {
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