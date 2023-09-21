package com.example.bookcourt.ui.recommendation

import BottomSheetCustom
import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.feedback.UserFeedback
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.notifications.NotificationNothingToShow
import com.example.bookcourt.ui.theme.LightGreyColor
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.utils.*
import com.skydoves.cloudy.Cloudy
import com.example.bookcourt.utils.Constants.LIMIT_WINDOW_HEIGHT

@Composable
fun RecommendationScreen(
    isNeedToUpdateFeedback:Boolean = false,
    description: String? = null,
    onNavigateToStatistics: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToLeaveFeedbackScreen: (title: String, rate: Int) -> Unit,
    onNavigateToFeedback: (title: String) -> Unit,
    viewModel: RecommendationViewModel = hiltViewModel(),
) {
    if(description != null){
        viewModel.description.value = description
    }
    viewModel.isNeedToUpdateFeedback.value = isNeedToUpdateFeedback

    RecommendationContent(
        onNavigateToStatistics,
        onNavigateToProfile,
        onNavigateToLeaveFeedbackScreen,
        onNavigateToFeedback,
        viewModel = viewModel
    )
}

@Composable
fun RecommendationContent(
    onNavigateToStatistics: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToLeaveFeedbackScreen: (title: String, rate: Int) -> Unit,
    onNavigateToFeedback: (title: String) -> Unit,
    viewModel: RecommendationViewModel,
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
                Cloudy(radius = 20) {
                    MainRecommendationContent(
                        viewModel,
                        frontItem,
                        backItem,
                        context,
                        windowHeight,
                        cardStackHeight,
                        onNavigateToProfile,
                        itemsIsNotEmpty,
                        onNavigateToLeaveFeedbackScreen,
                        onNavigateToFeedback,
                    )
                }

            } else {
                MainRecommendationContent(
                    viewModel,
                    frontItem,
                    backItem,
                    context,
                    windowHeight,
                    cardStackHeight,
                    onNavigateToProfile,
                    itemsIsNotEmpty,
                    onNavigateToLeaveFeedbackScreen,
                    onNavigateToFeedback
                )
            }


        }


    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainRecommendationContent(
    viewModel: RecommendationViewModel,
    frontItem: Book?,
    backItem: Book?,
    context: Context,
    windowHeight: Float,
    cardStackHeight: Dp,
    onNavigateToProfile: () -> Unit,
    itemsIsNotEmpty: Boolean,
    onNavigateToLeaveFeedbackScreen: (title: String, rate: Int) -> Unit,
    onNavigateToFeedback: (title: String) -> Unit,
) {
    val visibilityTopBarState = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .background(MainBgColor)
            .fillMaxSize(),
    ) {
        val screenWidth = with(LocalDensity.current) {
            LocalConfiguration.current.screenWidthDp.dp.toPx()
        }
        val screenHeight = with(LocalDensity.current) {
            LocalConfiguration.current.screenHeightDp.dp.toPx()
        }
        var controller = rememberBottomSheetController(screenWidth, screenHeight)


        if(controller.isExpanded.value){
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(65))
                    .background(Color(252, 225, 129))
                    .padding(top = 12.dp, bottom = 12.dp)
                    .clickable(
//                        interactionSource = Color { MutableInteractionSource() },
//                        indication = rememberRipple(color = Color.Black),
                    ) {
                        viewModel.addToBasket(
                            BasketItem(
                                data = frontItem!!,
                            )
                        )

                        DataClickMetric(
                            Buttons.BUY_BOOK,
                            Screens.Recommendation.route
                        )

//                        val sendIntent = Intent(
//                            Intent.ACTION_VIEW, Uri.parse(
//                                frontItem.buyUri
//                            )
//                        )
//                        val webIntent =
//                            Intent.createChooser(sendIntent, null)
//                        context.startActivity(webIntent)
                    }.align(Alignment.BottomCenter).zIndex(10f),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    RobotoRegularText(
                        text = "В корзину ${frontItem?.bookInfo?.price}₽",
                        fontSize = 16 )

                }

            }
        }




        if (itemsIsNotEmpty) {
            Column() {
                RecommendationTopBar(visibility = visibilityTopBarState.value,
                    title = frontItem?.bookInfo?.title,
                    onClickBackArrow = {
                        visibilityTopBarState.value = false
                        controller.hide()
                    },
                    rightIcons = {
                        Image(
                            painter = painterResource(id = R.drawable.favorite_book_topbar),
                            contentDescription = null,
                            modifier = Modifier,
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            painter = painterResource(id = R.drawable.share_icon),
                            contentDescription = null,
                            modifier = Modifier,
                            contentScale = ContentScale.Crop
                        )
                    })
                Box(modifier = Modifier) {
                    Spacer(modifier = Modifier.height(if (windowHeight > LIMIT_WINDOW_HEIGHT) 10.dp else 5.dp))
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
                            viewModel.isNeedToUpdateFeedback.value = false
                            viewModel.rate.value = 0

                        },
                        onSwipeRight = {
                            with(viewModel) {
                                metricSwipeRight(it)
                                validBooks.remove(it)
                            }
                            viewModel.counter += 1
                            viewModel.isNeedToUpdateFeedback.value = false
                            viewModel.rate.value = 0

                        },
                        onSwipeUp = {
                            with(viewModel) {
                                metricSwipeTop(it)
                                validBooks.remove(it)
                            }
                            viewModel.counter += 1
                            viewModel.isNeedToUpdateFeedback.value = false
                            viewModel.rate.value = 0

                        },
                        onSwipeDown = {
                            with(viewModel) {
                                metricSwipeDown(it)
                                validBooks.remove(it)
                            }
                            viewModel.counter += 1
                            viewModel.isNeedToUpdateFeedback.value = false
                            viewModel.rate.value = 0

                        },
                        disableDraggable = viewModel.isFirstNotification.value
                    )


                }
            }

            frontItem?.bookInfo?.price?.let {
                BottomSheetCustom(
                    price = it,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    sheetContent = {
                        if (frontItem != null) {
                            Column(
                                modifier = Modifier
                                    .background(MainBgColor)
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 12.dp,
                                        bottom = 10.dp
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                    //.height(60.dp)
                                    ,
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
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
                                    Row(
                                        modifier = Modifier
                                            .padding()
                                            .fillMaxWidth()
                                        //.fillMaxHeight()
                                        ,
                                        horizontalArrangement = Arrangement.Center
                                    ) {

                                        Text(
                                            modifier = Modifier,
                                            text = "${frontItem.bookInfo.author} | ${frontItem.bookInfo.genre}",
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
                                            .height(50.dp)
                                            .width(50.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )


                                }
                                Spacer(
                                    modifier = Modifier
                                        .height(12.dp)
                                        .fillMaxWidth()
                                )
                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
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
                                                text = "124",
                                                color = Color.Black,
                                                fontFamily = FontFamily(
                                                    Font(
                                                        R.font.roboto_medium,
                                                    )
                                                ),
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = "Дизлайки",
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
                                                text = "56",
                                                color = Color.Black,
                                                fontFamily = FontFamily(
                                                    Font(
                                                        R.font.roboto_medium,
                                                    )
                                                ),
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = "Интересуются",
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
                                    }

                                    Spacer(
                                        modifier = Modifier
                                            .height(16.dp)
                                            .fillMaxWidth()
                                    )
                                    FeedbackBlock(
                                        frontItem = frontItem,
                                        isNeedToUpdateFeedback = viewModel.isNeedToUpdateFeedback.value,
                                        username = viewModel.username.value,
                                        date = viewModel.date.value,
                                        title = frontItem.bookInfo.title,
                                        description = viewModel.description.value,
                                        rate = viewModel.rate.value,
                                        leaveFeedbackVisibility = viewModel.leaveFeedbackVisibility.value,
                                        onNavigateToFeedback = onNavigateToFeedback,
                                        onClickRatingBar = {
                                            viewModel.rate.value = it
                                            viewModel.title.value  = frontItem.bookInfo.title
                                            onNavigateToLeaveFeedbackScreen(viewModel.title.value,viewModel.rate.value)
                                        },
                                        disableLeaveFeedbackVisibility = {
                                            viewModel.leaveFeedbackVisibility.value = false
                                        }
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .height(16.dp)
                                            .fillMaxWidth()
                                    )
                                    var list = listOf("Детективы", "Детская литература", "Рассказы")
                                    CategoriesBlock(list)
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
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Column() {
                                            RobotoRegularText(
                                                text = "Автор",
                                                fontSize = 16,
                                                color = LightGreyColor
                                            )
                                            RobotoRegularText(
                                                text = "Год издания",
                                                fontSize = 16,
                                                color = LightGreyColor
                                            )
                                            RobotoRegularText(
                                                text = "Издательство",
                                                fontSize = 16,
                                                color = LightGreyColor
                                            )
                                            RobotoRegularText(
                                                text = "Тип обложки",
                                                fontSize = 16,
                                                color = LightGreyColor
                                            )
                                            RobotoRegularText(
                                                text = "Страниц",
                                                fontSize = 16,
                                                color = LightGreyColor
                                            )
                                            RobotoRegularText(
                                                text = "Формат",
                                                fontSize = 16,
                                                color = LightGreyColor
                                            )
                                            RobotoRegularText(
                                                text = "Язык",
                                                fontSize = 16,
                                                color = LightGreyColor
                                            )
                                            RobotoRegularText(
                                                text = "ISBN",
                                                fontSize = 16,
                                                color = LightGreyColor
                                            )
                                            RobotoRegularText(
                                                text = "Остаток на полках",
                                                fontSize = 16,
                                                color = LightGreyColor
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(62.dp))
                                        Column() {
                                            RobotoRegularText(
                                                text = "Диана Лютер",
                                                fontSize = 16
                                            )
                                            RobotoRegularText(
                                                text = "2021 г.",
                                                fontSize = 16
                                            )
                                            RobotoRegularText(
                                                text = "Лютература",
                                                fontSize = 16
                                            )
                                            RobotoRegularText(
                                                text = "Переплет",
                                                fontSize = 16
                                            )
                                            RobotoRegularText(
                                                text = "152 ст.",
                                                fontSize = 16
                                            )
                                            RobotoRegularText(
                                                text = "Печатная книга",
                                                fontSize = 16
                                            )
                                            RobotoRegularText(
                                                text = "Русский",
                                                fontSize = 16
                                            )
                                            RobotoRegularText(
                                                text = "9785716410091",
                                                fontSize = 16,
                                            )
                                            RobotoRegularText(
                                                text = "38 шт.",
                                                fontSize = 16
                                            )
                                        }
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(36.dp)
                                            .fillMaxWidth()
                                    )

                                }


                            }
                        }
                    },
                    onExpanding = {
                        visibilityTopBarState.value = true
                    },
                    onCollapsing = {
                        visibilityTopBarState.value = false
                    },
                    controller = controller
                )
            }


        } else {
            NotificationNothingToShow(
                title = "На этом пока всё",
                text = "Вы можете ознакомиться с вашей статистикой пока мы подберем для вас новые книги"
            )
        }
    }

}


@Composable
fun RecommendationTopBar(
    visibility: Boolean,
    onClickBackArrow: () -> Unit,
    title: String?,
    rightIcons: @Composable () -> Unit,
) {
    if (visibility) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
        ) {
            Row(modifier = Modifier.align(Alignment.CenterStart)) {
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onClickBackArrow()

                    }
                )
            }
            if (title != null) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = title,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontSize = 18.sp
                )
            }
            Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                rightIcons()
            }
        }
    }


}

@Composable
fun CategoriesBlock(list: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (item in list) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(134, 134, 134))
                    .padding(16.dp, 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun FeedbackBlock(
    frontItem: Book,
    isNeedToUpdateFeedback:Boolean,
    username:String,
    date:String,
    rate: Int,
    description: String,
    title:String,
    onNavigateToFeedback: (title: String) -> Unit,
    leaveFeedbackVisibility: Boolean,
    onClickRatingBar: (rate:Int) -> Unit,
    disableLeaveFeedbackVisibility: () -> Unit
) {
    if (isNeedToUpdateFeedback){
        frontItem.feedbacks.leaveAFeedback(
            userFeedback = UserFeedback(
                username = username,
                description = description,
                rate=rate,
                date = date
            )
        )
    }
    if (!frontItem.feedbacks.isUserLeaveFeedback) {
        LeaveFeedback(
            rate = rate,
            disableLeaveFeedbackVisibility = disableLeaveFeedbackVisibility,
            title = title,
            onClickRatingBar = onClickRatingBar)
    }
    Spacer(modifier = Modifier.height(8.dp))
    FeedbackBar(onNavigateToFeedback, title)
    Spacer(modifier = Modifier.height(8.dp))
    if (frontItem.feedbacks.isUserLeaveFeedback) {
            frontItem.feedbacks.userFeedback?.let {
                Feedback(
                    it.username,
                    it.rate,
                    it.date,
                    it.description
                )
            }

    }


}

@Composable
fun FeedbackBar(
    onNavigateToFeedback: (title: String) -> Unit,
    title: String
) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20))
            .background(Color(247, 247, 247))
            .padding(8.dp)
            .clickable {
                onNavigateToFeedback(title)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.43f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.40f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.star_selected),
                        contentDescription = null
                    )
                    Text(
                        text = "5,0",
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        fontSize = 20.sp
                    )
                }
                RobotoRegularText(
                    text = "84 отзыва",
                    color = Color(140, 140, 140),
                    fontSize = 16
                )
            }
            Image(painter = painterResource(id = R.drawable.right_arrow), contentDescription = null)
        }
    }

}

@Composable
fun LeaveFeedback(
    rate: Int,
    title: String,
    onClickRatingBar: (rate: Int) -> Unit,
    disableLeaveFeedbackVisibility: () -> Unit

) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20))
            .background(Color(247, 247, 247))
            .padding(top = 12.dp, bottom = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(0.60f)
        ) {
            Text(
                text = "Уже читали? Оцените книгу", fontFamily = FontFamily(
                    Font(
                        R.font.roboto_bold,
                    )
                ), fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            RatingBar(rate = rate , enabled = true, onClick = {
                onClickRatingBar(it)
            }, disableLeaveFeedbackVisibility = disableLeaveFeedbackVisibility)
        }
    }


}

@Composable
fun RatingBar(
    rate:Int = 0,
    enabled: Boolean,
    numberOfSelectedStarsDefault: Int = 0,
    starSize: Int = 29,
    onClick: (rate: Int) -> Unit,
    disableLeaveFeedbackVisibility: () -> Unit
) {
    val numberOfStars = 5;
    val numberOfSelectedStars = remember { mutableStateOf(numberOfSelectedStarsDefault) }
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        for (i in 1..rate) {
            Image(
                painter = painterResource(id = R.drawable.star_selected),
                contentDescription = null,
                modifier = Modifier
                    .clickable(enabled = enabled) {
                        onClick(i)
                    }
                    .size(starSize.dp)
            )
        }
        for (i in (rate + 1)..numberOfStars) {
            Image(
                painter = painterResource(id = R.drawable.star_unselected),
                contentDescription = null,
                modifier = Modifier
                    .clickable(enabled = enabled) {
                        numberOfSelectedStars.value = i
                        disableLeaveFeedbackVisibility()
                        onClick(i)
                    }
                    .size(starSize.dp)
            )
        }
    }

}

@Composable
fun Feedback(
    username: String,
    rate: Int,
    date: String,
    description: String,
    bgColor: Color = Color(247, 247, 247)
) {
    val tedt =5
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10))
            .background(bgColor)
            .padding(12.dp, 12.dp)
    ) {
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.image_placeholder),
                        contentDescription = null,
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = username,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(
                                Font(R.font.roboto_regular)
                            ),
                        )
                        RatingBar(enabled = false,
                            rate = rate,
                            numberOfSelectedStarsDefault = rate,
                            starSize = 18,
                            onClick = {},
                            disableLeaveFeedbackVisibility ={},
                        )
                    }
                }
                Text(
                    text = date,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(R.font.roboto_regular)
                    ),
                    color = Color(147, 147, 147)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = description,
                fontSize = 16.sp,
                fontFamily = FontFamily(
                    Font(R.font.roboto_regular)
                ),
                lineHeight = 24.sp
            )
        }
    }
}


@Composable
fun RobotoRegularText(
    text:String,
    color:Color = Color.Black,
    fontSize:Int
){
    Text(
        text = text,
        color = color,
        fontSize = fontSize.sp,
        fontFamily = FontFamily(
            Font(
                R.font.roboto_regular,
            )
        )
    )
}



