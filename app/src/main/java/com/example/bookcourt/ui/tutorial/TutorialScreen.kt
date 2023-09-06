package com.example.bookcourt.ui.tutorial

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.models.ui.TutorialCard
import com.example.bookcourt.ui.theme.BackGroundWhite
import com.example.bookcourt.utils.CustomButton
import com.example.bookcourt.ui.theme.Gilroy
import com.example.bookcourt.utils.Buttons
import com.example.bookcourt.utils.Constants.tutorialCards
import com.example.bookcourt.utils.Screens
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield


@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorScreen(
    onNavigateToCategorySelection: () -> Unit,
    mViewModel: TutorialScreenViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = tutorialCards.size, initialOffscreenLimit = 2)

    LaunchedEffect(key1 = Unit) {
        while (true) {
            yield()
            delay(1300)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % (pagerState.pageCount),
                animationSpec = tween(700)
            )
        }
    }

    val tutorialCard = tutorialCards[(pagerState.currentPage)]
    val nextPage = tutorialCards[(pagerState.currentPage + 1) % (pagerState.pageCount)]

    var exitAnim: ExitTransition

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(BackGroundWhite)
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ) {
        Box(modifier = Modifier)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box {
                Box {
                    Box(
                        modifier = Modifier
                            .height(490.dp)
                            .width(382.dp)
                            .zIndex(1f)
                    ) {
                        val iconSize = 70
                        when (tutorialCard.swipe) {
                            "Right" -> {
                                exitAnim = slideOutHorizontally(
                                    targetOffsetX = { -1000 },
                                    animationSpec = tween(300)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.right_icon_ic),
                                        contentDescription = "Swipe Right",
                                        alignment = Alignment.TopCenter,
                                        modifier = Modifier
                                            .size((iconSize + 10).dp)
                                            .alpha(0.9f)
                                            .zIndex(2f)
                                    )
                                }
                            }
                            "Left" -> {
                                exitAnim = slideOutHorizontally(
                                    targetOffsetX = { 1000 },
                                    animationSpec = tween(300)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.left_icon_ic),
                                        contentDescription = "Swipe Left",
                                        alignment = Alignment.CenterEnd,
                                        modifier = Modifier
                                            .size((iconSize + 10).dp)
                                            .alpha(0.9f)
                                            .zIndex(2f)
                                    )
                                }
                            }
                            "Up" -> {
                                exitAnim = slideOutVertically(
                                    targetOffsetY = { -1000 },
                                    animationSpec = tween(300)
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.up_icon_ic),
                                        contentDescription = "Swipe Up",
                                        alignment = Alignment.CenterStart,
                                        modifier = Modifier
                                            .size(iconSize.dp)
                                            .alpha(0.9f)
                                            .zIndex(2f)
                                    )
                                }
                            }
                            "Down" -> {

                            }
                            else -> {
                                exitAnim = slideOutVertically(
                                    targetOffsetY = { 1000 },
                                    animationSpec = tween(300)
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .zIndex(0f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .height(490.dp)
                                .width(260.dp)
                                .padding(top = 39.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.Black)
                        ) {
                            val animationSpec = 700
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(5.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(BackGroundWhite)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.status_bar),
                                    contentDescription = "Status Bar",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .padding(top = 2.dp, start = 10.dp, end = 10.dp)
                                )
                                when (tutorialCard.swipe) {
                                    "Right" -> {
                                        exitAnim = slideOutHorizontally(
                                            targetOffsetX = { -1000 },
                                            animationSpec = tween(animationSpec)
                                        )
                                        Cover(tutorialCard, nextPage, exitAnim)
                                    }
                                    "Left" -> {
                                        exitAnim = slideOutVertically(
                                            targetOffsetY = { -1000 },
                                            animationSpec = tween(animationSpec)
                                        )
                                        Cover(tutorialCard, nextPage, exitAnim)
                                    }
                                    "No" -> {
                                        exitAnim = slideOutHorizontally(
                                            targetOffsetX = { 1000 },
                                            animationSpec = tween(animationSpec)
                                        )
                                        Cover(tutorialCard, nextPage, exitAnim)
                                    }
                                    "Up" -> {
                                        exitAnim = slideOutVertically(
                                            targetOffsetY = { 1000 },
                                            animationSpec = tween(animationSpec)
                                        )
                                        Cover(tutorialCard, nextPage, exitAnim)
                                    }
                                    else -> {
                                        exitAnim = fadeOut(
                                            animationSpec = tween(animationSpec)
                                        )
                                        Cover(tutorialCard, nextPage, exitAnim)
                                    }
                                }
                                BottomPart(
                                    bookTitle = tutorialCard.bookTitle,
                                    bookAuthor = tutorialCard.bookAuthor
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (tutorialCard.bottomIcon != null) {
                                Image(
                                    painter = painterResource(id = tutorialCard.bottomIcon),
                                    contentDescription = "Bottom Icon",
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(
                                text = tutorialCard.bottomText,
                                fontSize = 15.sp,
                                fontFamily = Gilroy
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                }
            }
        }
        CustomButton(
            text = "Супер, понятно",
            textColor = Color.Black,
            color = Color(0xFFFCE181),
            onCLick = {
                mViewModel.changeIsTutorChecked()
                mViewModel.metricClick(DataClickMetric(Buttons.CHECK_TUTOR, Screens.Tutorial.route))
                onNavigateToCategorySelection()
            }
        )
    }
}

@Composable
fun Cover(
    page: TutorialCard,
    nextPage: TutorialCard,
    animation: ExitTransition = fadeOut()
) {
    var isReady by remember { mutableStateOf(true) }
    var secondReady by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        isReady = true
        secondReady = false
        delay(1300)
        isReady = false
        secondReady = true
    }

    Box {
        AnimatedVisibility(
            visible = isReady,
            exit = animation
        ) {
            Box(
                modifier = Modifier
                    .height(320.dp)
                    .width(220.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .zIndex(2f)
            ) {
                Image(
                    painter = painterResource(id = page.cover),
                    contentDescription = "Book Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
        AnimatedVisibility(
            visible = secondReady,
            enter = fadeIn(
                animationSpec = tween(700)
            )
        ) {
            Box(
                modifier = Modifier
                    .height(320.dp)
                    .width(220.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .zIndex(0f)
            ) {
                Image(
                    painter = painterResource(id = nextPage.cover),
                    contentDescription = "Book Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun BottomPart(
    bookTitle: String,
    bookAuthor: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(Color.White),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .width(50.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color(0xFFEEECEC))
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 16.dp, top = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
            ) {
                Text(
                    text = bookTitle,
                    fontFamily = Gilroy,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = bookAuthor,
                    fontFamily = Gilroy,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF7F7F7))
                    .padding(3.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.luterature_logo),
                    contentDescription = "Sponsor Logo",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}