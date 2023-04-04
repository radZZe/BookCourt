package com.example.bookcourt.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.bookcourt.R
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.recommendation.RecomendationViewModel
import com.example.bookcourt.ui.theme.CustomButton
import com.example.bookcourt.utils.*

@Composable
fun RecomendationScreen(onNavigateToStatistics: () -> Unit) {
    RecomendationContent(onNavigateToStatistics)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecomendationContent(
    onNavigateToStatistics: () -> Unit,
    viewModel: RecomendationViewModel = hiltViewModel()
) {
    var windowHeight =  LocalConfiguration.current.screenHeightDp.toFloat() * LocalDensity.current.density

    var cardStackHeight = if(windowHeight > LIMIT_WINDOW_HEIGHT) 550.dp else 480.dp
    var topBarHeight = if(windowHeight > LIMIT_WINDOW_HEIGHT) 45.dp else 30.dp

    var counter = viewModel.counter
    var limitSwipeValue = viewModel.limitSwipeValue
    var context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        if (viewModel.isFirstDataLoading && viewModel.validBooks.isEmpty()) {
            viewModel.getAllBooks(context)
        }
    }

    val isNotificationDisplay = viewModel.isNotificationDisplay.collectAsState(initial = "")

    if (counter == limitSwipeValue && isNotificationDisplay.value == false) {
        viewModel.countEqualToLimit()
    }
    if (counter == limitSwipeValue + 1) {
        viewModel.isFirstNotification.value = false
    }

    if (viewModel.dataIsReady) {


        Box(modifier = Modifier.fillMaxSize()) {
            if (viewModel.isFirstNotification.value) {
                viewModel.displayNotificationMessage()
                NotificationMessage(
                    navigateToStatistics = {
                        onNavigateToStatistics()
                        viewModel.closeNotificationMessage()
                        viewModel.isFirstNotification.value = false

                    },
                    closeCallback = {
                        viewModel.closeNotificationMessage()
                        viewModel.isFirstNotification.value = false
                    }
                )
            }
            Column(
                modifier = Modifier
                    .background(Color(red = 250, 248, 242))
                    .blur(viewModel.blurValueRecommendationScreen.value.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.validBooks.isNotEmpty()) {
                    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                        bottomSheetState = rememberBottomSheetState(
                            initialValue = BottomSheetValue.Collapsed
                        )
                    )
                    BottomSheetScaffold(
                        sheetContent = {
                            var item = viewModel.validBooks.last()
                            Column(
                                modifier = Modifier
                                    .height(446.dp)
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 12.dp,
                                        bottom = 10.dp
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.1f)
                                        .height(2.dp)
                                        .background(Color.Gray)
                                        .clip(RoundedCornerShape(50))
                                ) {}
                                Spacer(
                                    modifier = Modifier
                                        .height(18.dp)
                                        .fillMaxWidth()
                                )
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(60.dp),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding()
                                            .fillMaxWidth()
                                            .fillMaxHeight(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = item.bookInfo.title,
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                                fontFamily = FontFamily(
                                                    Font(
                                                        R.font.roboto_bold,
                                                    )
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                modifier = Modifier,
                                                text = "${item.bookInfo.author}, ${item.bookInfo.genre}",
                                                color = Color(134, 134, 134),
                                                fontSize = 14.sp,
                                                fontFamily = FontFamily(
                                                    Font(
                                                        R.font.roboto_regular,
                                                    )
                                                )
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = R.drawable.igra_slov_logo),
                                            contentDescription = "Logo image",
                                            modifier = Modifier
                                                .height(60.dp)
                                                .width(60.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )

                                    }


                                }
                                Spacer(
                                    modifier = Modifier
                                        .height(16.dp)
                                        .fillMaxWidth()
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "423",
                                            color = Color.Black,
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.roboto_medium,
                                                )
                                            ),
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "Лайки",
                                            color = Color(134, 134, 134),
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.roboto_medium,
                                                )
                                            ),
                                            fontSize = 14.sp
                                        )
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = item.bookInfo.rate.toString(),
                                            color = Color.Black,
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.roboto_medium,
                                                )
                                            ),
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "Оценка",
                                            color = Color(134, 134, 134),
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.roboto_medium,
                                                )
                                            ),
                                            fontSize = 14.sp
                                        )
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = item.bookInfo.numberOfPages,
                                            color = Color.Black,
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.roboto_medium,
                                                )
                                            ),
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "Страниц",
                                            color = Color(134, 134, 134),
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.roboto_medium,
                                                )
                                            ),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                                Spacer(
                                    modifier = Modifier
                                        .height(16.dp)
                                        .fillMaxWidth()
                                )
                                Column(modifier = Modifier.height(134.dp)) {
                                    Text(
                                        text = "Описание",
                                        color = Color.Black,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.roboto_bold,
                                            )
                                        ),
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                    Text(
                                        text = item.bookInfo.description,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.roboto_regular,
                                            )
                                        ),
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        maxLines = 5,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Spacer(
                                    modifier = Modifier
                                        .height(16.dp)
                                        .fillMaxWidth()
                                )
                                Box( // Поменять на CustomButton
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(65))
                                        .background(Color(252, 225, 129))
                                        .padding(top = 12.dp, bottom = 12.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = rememberRipple(color = Color.Black),
                                        ) {

                                            DataClickMetric(
                                                Buttons.BUY_BOOK, Screens.Recommendation.route
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
                                        text = "Посмотреть в магазине",
                                        color = Color.Black,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.roboto_regular,
                                            )
                                        ),
                                        fontSize = 16.sp,
                                    )
                                }

                            }

                        },
                        sheetContentColor = Color.Green,
                        sheetElevation = 5.dp,
                        sheetShape = RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp),
                        sheetPeekHeight = if(windowHeight>LIMIT_WINDOW_HEIGHT) 175.dp else 155.dp,
                        scaffoldState = bottomSheetScaffoldState
                    ) {
                        Box(){
                            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .alpha(0.3f)
                                        .background(Color.Black)
                                        .zIndex(5f)
                                )
                            }
                            Column() {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(topBarHeight)
                                        .zIndex(4f),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.statistics_icon),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .clickable {
                                                onNavigateToStatistics()
                                                viewModel.metricClick(
                                                    DataClickMetric(
                                                        button = "Statistics",
                                                        screen = "Recommendation"
                                                    )
                                                )
                                            }
                                            .padding(end = 8.dp)
                                    )
                                    Image(
                                        painter = painterResource(id = R.drawable.search_icon),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .clickable {
                                                //TODO ПОИСК
                                                viewModel.metricClick(
                                                    DataClickMetric(
                                                        button = "Search",
                                                        screen = "Recommendation"
                                                    )
                                                )
                                            }
                                            .padding(end = 18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(if(windowHeight > LIMIT_WINDOW_HEIGHT) 25.dp else 15.dp))
                                CardStack(
                                    modifier = Modifier.height(cardStackHeight),
                                    user = viewModel.user,
                                    itemsRaw = viewModel.validBooks,
                                    onSwipeLeft = {
                                        with(viewModel) {
                                            metricSwipeLeft(it)
                                            validBooks.remove(it)
                                            // counter += 1
                                        }
                                        viewModel.counter += 1

                                    },
                                    onSwipeRight = {
                                        with(viewModel) {
                                            metricSwipeRight(it)
                                            validBooks.remove(it)
                                            // counter += 1
                                        }
                                        viewModel.counter += 1

                                    },
                                    onSwipeUp = {
                                        with(viewModel) {
                                            metricSwipeTop(it)
                                            validBooks.remove(it)
                                            // counter += 1
                                        }
                                        viewModel.counter += 1

                                    },
                                    onSwipeDown = {
                                        with(viewModel) {
                                            metricSwipeDown(it)
                                            validBooks.remove(it)
                                            // counter += 1
                                        }
                                        viewModel.counter += 1

                                    },
                                    onNavigateToStatistics = onNavigateToStatistics
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
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
                            viewModel.metricClick(
                                DataClickMetric(
                                    Buttons.OPEN_STATS,
                                    Screens.Recommendation.route
                                )
                            )
                            viewModel.metricScreenTime()
                            onNavigateToStatistics()
                        }
                    }
                }

            }
        }

    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

}

@Composable
fun NotificationMessage(navigateToStatistics: () -> Unit, closeCallback: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(4f)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
        ) {
            Image(
                painterResource(id = R.drawable.close_icon),
                contentDescription = "",
                modifier = Modifier.clickable {
                    closeCallback()
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Winking%20Face.png")
                    .size(Size.ORIGINAL)
                    .build(),
            )

            if (painter.state is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator()
            }
            Image(
                painter = painter,
                contentDescription = " ",
                contentScale = ContentScale.Crop,
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 15.dp)
            ) {
                Text(
                    text = "Ух ты! Вы посмотрели уже 3 книги", fontFamily = FontFamily(
                        Font(
                            R.font.manrope_extrabold,
                        )
                    ),
                    fontSize = 12.sp
                )
                Text(
                    text = "Теперь можно взглянуть на статистику", fontFamily = FontFamily(
                        Font(
                            R.font.manrope_extrabold,
                        )
                    ),
                    fontSize = 12.sp
                )
            }

            CustomButton(
                text = "Смотреть статистику",
                textColor = Color.Black,
                color = Color(252, 225, 129),
                onCLick = {
                    navigateToStatistics()
                })
        }
    }

}





