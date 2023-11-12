package com.example.bookcourt.ui.recommendation

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.feedback.UserFeedback
import com.example.bookcourt.ui.notifications.NotificationNothingToShow
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.ui.theme.dimens
import com.example.bookcourt.utils.*
import com.skydoves.cloudy.Cloudy
import com.example.bookcourt.utils.Constants.LIMIT_WINDOW_HEIGHT

@Composable
fun RecommendationScreen(
    onNavigateToStatistics: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateBookCard:(bookId:String)->Unit,
    viewModel: RecommendationViewModel = hiltViewModel(),
) {
    val isAuthenticated = viewModel.isAuthenticated.collectAsState(initial = false)

    if(isAuthenticated.value){
        RecommendationContent(
            onNavigateToStatistics,
            onNavigateToProfile,
            viewModel = viewModel,
            onNavigateBookCard
        )
    }else{
        Box(modifier = Modifier.fillMaxSize().padding(20.dp), contentAlignment = Alignment.Center){
            Text(text = "Вам необходимо авторизоваться для того чтоб вам были доступны рекомендации")
        }
    }



}

@Composable
fun RecommendationContent(
    onNavigateToStatistics: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: RecommendationViewModel,
    onNavigateBookCard:(bookJson:String)->Unit,
) {

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
                        itemsIsNotEmpty,
                        onNavigateBookCard
                    )
                }

            } else {
                MainRecommendationContent(
                    viewModel,
                    frontItem,
                    backItem,
                    itemsIsNotEmpty,
                    onNavigateBookCard
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
    itemsIsNotEmpty: Boolean,
    onNavigateBookCard:(bookJson:String)->Unit,
) {
    Box(
        modifier = Modifier
            .background(MainBgColor)
            .fillMaxSize(),
    ) {

        if (itemsIsNotEmpty) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    //Spacer(modifier = Modifier.height(10.dp ))
                    CardStack(
                        modifier = Modifier
                            .fillMaxHeight(0.75f)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                if (frontItem != null) {
                                    onNavigateBookCard(frontItem.isbn!!)
                                }

                            },
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
                if (frontItem != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(0.25f)
                            .background(MainBgColor)
                            .padding(
                                start = MaterialTheme.dimens.paddingBig.dp,
                                end = MaterialTheme.dimens.paddingBig.dp,
                                top = MaterialTheme.dimens.paddingNormal.dp,
                                bottom = 0.dp
                            )
                            .align(Alignment.BottomCenter),
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
                                style = MaterialTheme.typography.body2,
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
                                    style = MaterialTheme.typography.subtitle1
                                )


                            }
                            Image(
                                painter = painterResource(id = R.drawable.igra_slov_logo),
                                contentDescription = "Logo image",
                                modifier = Modifier
                                    .height(MaterialTheme.dimens.iconSizeBig.dp)
                                    .width(MaterialTheme.dimens.iconSizeBig.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )


                        }
                        Spacer(
                            modifier = Modifier
                                .height(MaterialTheme.dimens.paddingSmall.dp)
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
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                    Text(
                                        text = "Лайки",
                                        color = Color(134, 134, 134),
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                }
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "124",
                                        color = Color.Black,
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                    Text(
                                        text = "Дизлайки",
                                        color = Color(134, 134, 134),
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                }
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "56",
                                        color = Color.Black,
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                    Text(
                                        text = "Интересуются",
                                        color = Color(134, 134, 134),
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                }
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = frontItem.bookInfo.rate.toString(),
                                        color = Color.Black,
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                    Text(
                                        text = "Оценка",
                                        color = Color(134, 134, 134),
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.paddingSmall.dp))
                    }
                }

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
                    modifier = Modifier.clickable(interactionSource =  MutableInteractionSource(),
                        indication = null) {
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
                    .padding(14.dp, 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.h5,
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
    disableLeaveFeedbackVisibility: () -> Unit,
    isAuthenticated:Boolean
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
    if (!frontItem.feedbacks.isUserLeaveFeedback && isAuthenticated) {
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
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
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
                        modifier = Modifier.size(MaterialTheme.dimens.iconSizeSmall.dp),
                        painter = painterResource(id = R.drawable.star_selected),
                        contentDescription = null
                    )
                    Text(
                        text = "5,0",
                        style = MaterialTheme.typography.h3,
                    )
                }
                Text(text="84 отзыва",
                    style = MaterialTheme.typography.subtitle1,
                    color = Color(140, 140, 140),)
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
                text = "Уже читали? Оцените книгу", style=MaterialTheme.typography.h4,
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
    starSize: Int = MaterialTheme.dimens.bookCardRatingBarSize,
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
                    .clickable(
                        enabled = enabled, interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
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
                    .clickable(
                        enabled = enabled, interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
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
                            starSize = MaterialTheme.dimens.listFeedbacksRatingBarSize,
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