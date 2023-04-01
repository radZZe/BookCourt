package com.example.bookcourt.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.bookcourt.ui.recomendation.RecomendationViewModel
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

                    BottomSheetScaffold(
                        sheetContent = {
                            var item = viewModel.validBooks.last()
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight(0.5f)
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
                                Spacer(modifier = Modifier
                                    .height(15.dp)
                                    .fillMaxWidth())
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.17f),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(bottom = 15.dp)
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
                                                        R.font.manrope_extrabold,
                                                        weight = FontWeight.W600
                                                    )
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                modifier = Modifier,
                                                text = "${item.bookInfo.author}, ${item.bookInfo.genre}",
                                                color = Color(134, 134, 134),
                                                fontSize = 13.sp,
                                                fontFamily = FontFamily(
                                                    Font(
                                                        R.font.manrope_medium,
                                                        weight = FontWeight.W600
                                                    )
                                                )
                                            )
                                        }
                                        Image(
                                            painter = painterResource(id = R.drawable.igra_slov_logo),
                                            contentDescription = "Logo image",
                                            modifier = Modifier
                                                .fillMaxHeight(1f)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )

                                    }


                                }
                                Box( // Поменять на CustomButton
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.15f)
                                        .clip(RoundedCornerShape(65))
                                        .background(Color(252, 225, 129))
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
                                        text = "В магазин ${item.bookInfo.price}р",
                                        color = Color.Black,
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.manrope_medium, weight = FontWeight.W400
                                            )
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier
                                    .height(15.dp)
                                    .fillMaxWidth())
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
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
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.manrope_extrabold,
                                                )
                                            )
                                        )
                                        Text(
                                            text = "Лайки",
                                            color = Color(134, 134, 134),
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.manrope_extrabold,
                                                )
                                            ),
                                            fontSize = 13.sp
                                        )
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = item.bookInfo.rate.toString(),
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.manrope_extrabold,
                                                )
                                            )
                                        )
                                        Text(
                                            text = "Оценка",
                                            color = Color(134, 134, 134),
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.manrope_extrabold,
                                                )
                                            ),
                                            fontSize = 13.sp
                                        )
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = item.bookInfo.numberOfPages,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.manrope_extrabold,
                                                )
                                            )
                                        )
                                        Text(
                                            text = "Страниц",
                                            color = Color(134, 134, 134),
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.manrope_extrabold,
                                                )
                                            ),
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier
                                    .height(15.dp)
                                    .fillMaxWidth())
                                Column() {
                                    Text(
                                        text = "Описание",
                                        color = Color.Black,
                                        fontFamily = FontFamily(
                                            Font(
                                                R.font.manrope_extrabold,
                                                weight = FontWeight.W600
                                            )
                                        ),
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                    Text(
                                        text = item.bookInfo.description,
                                        color = Color.Black,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                            }

                        },
                        sheetContentColor = Color.Green,
                        sheetElevation = 5.dp,
                        sheetShape = RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp),
                        sheetPeekHeight = 120.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.07f)
                                .background(
                                    Color.White
                                ),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.statistics_icon),
                                contentDescription = "",
                                modifier = Modifier
                                    .clickable {
                                        onNavigateToStatistics()
                                        viewModel.metricClick(DataClickMetric(button= "Statistics",screen="Recommendation"))
                                    }
                                    .padding(end = 8.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.search_icon),
                                contentDescription = "",
                                modifier = Modifier
                                    .clickable {
                                        //TODO ПОИСК
                                        viewModel.metricClick(DataClickMetric(button= "Search",screen="Recommendation"))
                                    }
                                    .padding(end = 18.dp)
                            )
                        }
                        CardStack(
                            modifier = Modifier.fillMaxHeight(0.8f),
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





