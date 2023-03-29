package com.example.bookcourt.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookcourt.R
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.statistics.StatisticsScreenRequest.PARTNER
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.Buttons
import com.example.bookcourt.utils.Buttons.CLOSE
import com.example.bookcourt.utils.Constants
import com.example.bookcourt.utils.Screens
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun Statistics(
    mViewModel: StatisticsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        mViewModel.getUserStats()
    }

    val stories = Constants.statisticScreensList.size
    val pagerState = rememberPagerState(pageCount = stories)
    val coroutineScope = rememberCoroutineScope()

    var currentPage by remember {
        mutableStateOf(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        StoryLikePages(pagerState = pagerState, Constants.statisticScreensList)

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
//            Box(modifier = Modifier.height(24.dp).fillMaxWidth().zIndex(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(2f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.padding(4.dp))
                for (i in 0 until stories) {
                    InstaStoriesProgressBar(
                        modifier = Modifier.weight(1f),
                        startProgress = (i == currentPage)
                    ) {
                        coroutineScope.launch {
                            if (currentPage < stories - 1) {
                                currentPage++
                            }
                            pagerState.animateScrollToPage(page = currentPage % stories)
                        }
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close_square),
                    contentDescription = "Close Icon",
                    tint = Color.LightGray,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable {
                            mViewModel.metricScreenTime()
                            mViewModel.sendOnClickMetric(
                                DataClickMetric(
                                    button = CLOSE,
                                    screen = "Statistics"
                                )
                            )
                        }
                )
            }
        }
    }
}

@Composable
fun ReadBooksStats() {
    val string = "книг"
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clip(RoundedCornerShape(30.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LighterPinkBackground),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
//            Image(
//                painter = painterResource(id = R.drawable.book_court_logo),
//                contentDescription ="Book court logo"
//            )
            Text(
                text = "0!",
                fontFamily = Inter,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                fontSize = 32.sp
            )
            Text(
                text = "Вы прочитали, хороший результат \uD83D\uDCAA",
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 18.sp,
            )
            Column {
                Text(
                    text = "Продолжайте читать, ведь чтение книг:",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    fontSize = 18.sp,
                )
                Text(
                    text = "1. Увеличивает словарный запас\n" +
                            "2. Помогает общаться с людьми\n" +
                            "3. Снижает стресс\n" +
                            "4. Развивает память и мышление",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp,
                )
            }
//            Image(
//                painter = painterResource(id = R.drawable.cup_coffee_open_book),
//                contentDescription = "lyuteratura logo",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.3f),
//                contentScale = ContentScale.Fit
//            )
            Button(
                onClick = {/*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(60.dp))
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(LightYellowBtn)
            ) {
                Text(text = "Поделиться")
            }
        }
    }
}

@Composable
private fun ReadBooksStats(
    navController: NavController,
    mViewModel: StatisticsViewModel = hiltViewModel()
) {
    val booksAmount = mViewModel.readBooks.value?.size
    val string = if (booksAmount == 1) "книга" else if (booksAmount in 2..4) "книги" else "книг"
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clip(RoundedCornerShape(30.dp))
            .clickable {
                mViewModel.sendOnClickMetric(
                    DataClickMetric(
                        Buttons.SWAP_STAT,
                        Screens.Statistics.route
                    )
                )
                mViewModel.currentScreen.value = PARTNER
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LighterPinkBackground),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
//            TopBar(navController, AMOUNT_OF_BOOKS)
//            Image(
//                painter = painterResource(id = R.drawable.book_court_logo),
//                contentDescription ="Book court logo"
//            )
            Text(
                text = "$booksAmount $string!",
                fontFamily = Inter,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                fontSize = 32.sp
            )
            Text(
                text = "Вы прочитали, хороший результат \uD83D\uDCAA",
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 18.sp,
            )
            Column {
                Text(
                    text = "Продолжайте читать, ведь чтение книг:",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    fontSize = 18.sp,
                )
                Text(
                    text = "1. Увеличивает словарный запас\n" +
                            "2. Помогает общаться с людьми\n" +
                            "3. Снижает стресс\n" +
                            "4. Развивает память и мышление",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp,
                )
            }
//            Image(
//                painter = painterResource(id = R.drawable.cup_coffee_open_book),
//                contentDescription = "lyuteratura logo",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.3f),
//                contentScale = ContentScale.Fit
//            )
            Button(
                onClick = {/*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(60.dp))
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(LightYellowBtn)
            ) {
                Text(text = "Поделиться")
            }
        }
    }
}

object StatisticsScreenRequest {
    const val AMOUNT_OF_BOOKS = "AMOUNT_OF_BOOKS_RQ"
    const val PARTNER = "partner lyuteratura"
    const val FAVORITE_GENRES = "FAVORITE_GENRES_RQ"
    const val FAVORITE_AUTHORS = "FAVORITE_AUTHORS_RQ"
}


//@Composable
//@Preview
//private fun FavoriteGenresStats(
//) {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(10.dp)
//            .clip(RoundedCornerShape(60.dp))
//            .background(Color(0xFF524E4E))
////            .clickable {
////                mViewModel.sendOnClickMetric(
////                    DataClickMetric(
////                        Buttons.SWAP_STAT,
////                        Screens.Statistics.route
////                    )
////                )
////                mViewModel.currentScreen.value = AMOUNT_OF_BOOKS
////            }
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
////            TopBar(navController = navController, rq = FAVORITE_GENRES)
//            Text(
//                text = "Мои любимые\nжанры",
//                fontFamily = Manrope,
//                fontWeight = FontWeight.ExtraBold,
//                color = Color.White,
//                fontSize = 32.sp,
//                modifier = Modifier.padding(start = 45.dp)
//            )
////            DisplayGenresShelves(mViewModel)
////            ShareApp(textColor = Color.White)
//        }
//    }
//}

@Composable
@Preview
fun jopa() {

}

