package com.example.bookcourt.ui.recommendation

import android.content.ContentResolver
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
import coil.compose.AsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.notifications.NotificationNothingToShow
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.utils.*
import com.example.bookcourt.utils.Constants.LIMIT_WINDOW_HEIGHT

@Composable
fun RecommendationScreen(
    onNavigateToStatistics: () -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    RecommendationContent(onNavigateToStatistics, onNavigateToProfile)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecommendationContent(
    onNavigateToStatistics: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: RecommendationViewModel = hiltViewModel()
) {

    val windowHeight =
        LocalConfiguration.current.screenHeightDp.toFloat() * LocalDensity.current.density
    val cardStackHeight = if (windowHeight > LIMIT_WINDOW_HEIGHT) 550.dp else 480.dp
    val counter = viewModel.counter
    val limitSwipeValue = viewModel.limitSwipeValue
    val context = LocalContext.current

    val isNotificationDisplay = viewModel.isNotificationDisplay.collectAsState(initial = "")

    if (counter == limitSwipeValue && isNotificationDisplay.value == false) {
        viewModel.countEqualToLimit()
    }
    if (counter == limitSwipeValue + 1) {
        viewModel.isFirstNotification.value = false
    }

    val resources = LocalContext.current.resources
    val imagePlaceholderUri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(R.drawable.image_placeholder))
        .appendPath(resources.getResourceTypeName(R.drawable.image_placeholder))
        .appendPath(resources.getResourceEntryName(R.drawable.image_placeholder))
        .build()

    LaunchedEffect(key1 = Unit) {
        if (viewModel.isFirstDataLoading && viewModel.validBooks.isEmpty()) {
            viewModel.getAllBooks(context)
        }
    }

    if (viewModel.dataIsReady) {

        val itemsIsNotEmpty = viewModel.validBooks.isNotEmpty()
        val frontItem =
            if (itemsIsNotEmpty) viewModel.validBooks[viewModel.validBooks.lastIndex] else null
        val backItem =
            if (viewModel.validBooks.size <= 1) null else viewModel.validBooks[viewModel.validBooks.lastIndex - 1]
        if (itemsIsNotEmpty) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (viewModel.isFirstNotification.value) {
                    viewModel.displayNotificationMessage()
                    NotificationRecommendation(
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
                        .background(MainBgColor)
                        .blur(viewModel.blurValueRecommendationScreen.value.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                        bottomSheetState = rememberBottomSheetState(
                            initialValue = BottomSheetValue.Collapsed
                        )
                    )

                    BottomSheetScaffold(
                        sheetContent = {
                            if (frontItem != null) {
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
                                                    text = frontItem.bookInfo.title,
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
                                                    text = "${frontItem.bookInfo.author}, ${frontItem.bookInfo.genre}",
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
                                                text = frontItem.bookInfo.rate.toString(),
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
                                                text = frontItem.bookInfo.numberOfPages,
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
                                            text = frontItem.bookInfo.description,
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
                                    Box(
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
                                                    Buttons.BUY_BOOK,
                                                    Screens.Recommendation.route
                                                )

                                                val sendIntent = Intent(
                                                    Intent.ACTION_VIEW, Uri.parse(
                                                        frontItem.buyUri
                                                    )
                                                )
                                                val webIntent =
                                                    Intent.createChooser(sendIntent, null)
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
                            }


                        },
                        sheetElevation = 5.dp,
                        sheetShape = RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp),
                        sheetPeekHeight = if (windowHeight > LIMIT_WINDOW_HEIGHT) 175.dp else 155.dp,
                        scaffoldState = bottomSheetScaffoldState,
                        sheetGesturesEnabled = !viewModel.isFirstNotification.value
                    ) {
                        Box(modifier = Modifier.background(MainBgColor)) {
                            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .alpha(0.3f)
                                        .background(Color.Black)
                                        .zIndex(5f)
                                )
                            }
                            Column {
                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(modifier = Modifier
                                        .clickable(
                                            enabled = !viewModel.isFirstNotification.value
                                        ) {
                                            viewModel.metricClick(
                                                DataClickMetric(
                                                    button = "ProfileButton",
                                                    screen = "Recommendation"
                                                )
                                            )
                                            viewModel.metricScreenTime()
                                            onNavigateToProfile()
                                        }
                                        .size(45.dp)
                                        .clip(CircleShape))
                                    {
                                        AsyncImage(
                                            model = if (viewModel.user.image == null) imagePlaceholderUri else viewModel.user.image,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .align(Alignment.Center),
                                            contentDescription = "profile_placeholder",
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(if (windowHeight > LIMIT_WINDOW_HEIGHT) 25.dp else 15.dp))
                                CardStack(
                                    modifier = Modifier.height(cardStackHeight),
                                    user = viewModel.user,
                                    frontItem = frontItem,
                                    backItem = backItem,
                                    onSwipeLeft = {
                                        with(viewModel) {
                                            metricSwipeLeft(it)
                                            validBooks.remove(it)
                                        }
                                        viewModel.counter += 1

                                    },
                                    onSwipeRight = {
                                        with(viewModel) {
                                            metricSwipeRight(it)
                                            validBooks.remove(it)
                                        }
                                        viewModel.counter += 1

                                    },
                                    onSwipeUp = {
                                        with(viewModel) {
                                            metricSwipeTop(it)
                                            validBooks.remove(it)
                                        }
                                        viewModel.counter += 1

                                    },
                                    onSwipeDown = {
                                        with(viewModel) {
                                            metricSwipeDown(it)
                                            validBooks.remove(it)
                                        }
                                        viewModel.counter += 1

                                    },
                                    disableDraggable = viewModel.isFirstNotification.value
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                )
                            }
                        }

                    }

                }

            }
        } else {
            NotificationNothingToShow(
                title = "На этом пока всё",
                text = "Вы можете ознакомиться с вашей статистикой пока мы подберем для вас новые книги"
            )
        }

    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

}







