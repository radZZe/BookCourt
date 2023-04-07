package com.example.bookcourt.ui.statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.Buttons.CLOSE
import com.example.bookcourt.utils.Constants
import com.example.bookcourt.utils.Screens
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun Statistics(
    navController: NavController,
    mViewModel: StatisticsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        mViewModel.getUserStats()
    }

    val stories = Constants.statisticScreensList.size
    val pagerState = rememberPagerState(pageCount = stories)
    val coroutineScope = rememberCoroutineScope()
    val gradient = Brush.verticalGradient(listOf(Shadow, Color.Transparent))

    var currentPage by remember {
        mutableStateOf(0)
    }

    var paused by remember {
        mutableStateOf(false)
    }

    val targetOffset = 500
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
//                        try {
//                            paused = true
//                            awaitRelease()
//                        } finally {
//                            paused = false
//                        }
                    },
                    onTap = { offset ->
                        coroutineScope.launch {
                            if (currentPage < stories - 1) {
                                if (offset.x < targetOffset) { // 300
                                    if (currentPage == 0) currentPage = 0 else currentPage--
                                } else if (offset.x > targetOffset) { // 700
                                    currentPage++
                                }
                            } else if (currentPage == stories - 1) {
                                if (offset.x < targetOffset) { // 300
                                    currentPage--
                                } else if (offset.x > targetOffset) { // 700
                                    navController.popBackStack()
                                    navController.navigate(route = Screens.Recommendation.route)
                                }
                            }
                            pagerState.animateScrollToPage(page = currentPage)
                        }
                    }
                )
            }

    ) {
        StoryLikePages(pagerState = pagerState, Constants.statisticScreensList)
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(gradient)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        start = 4.dp,
                        end = 4.dp
                    )
                    .zIndex(3f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.padding(4.dp))
                for (i in 0 until stories) {
                    InstaStoriesProgressBar(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp)),
                        paused = paused,
                        startProgress = (i == currentPage),
                        isNext = (i > currentPage)
                    ) {
                        coroutineScope.launch {
                            if (currentPage < stories - 1) {
                                currentPage++
                            } else {
                                navController.popBackStack()
                                navController.navigate(route = Screens.Recommendation.route)
                            }
                            pagerState.animateScrollToPage(page = currentPage % stories)
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(top = 8.dp, start = 4.dp, end = 4.dp, bottom = 2.dp)
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, bottom = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close_icon),
                    contentDescription = "Close Icon",
                    tint = Color.White,
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
                            navController.popBackStack()
                            navController.navigate(route = Screens.Recommendation.route)
                        }

                )
            }
        }
    }
}

@Composable
fun FavoriteGenresStats(
    mViewModel: StatisticsViewModel = hiltViewModel()
) {
    val view = LocalView.current
    val context = LocalContext.current
    val topGenres = mViewModel.getTopGenres().toList()
    val top3Genres = listOf(
        topGenres.getOrNull(0),
        topGenres.getOrNull(1),
        topGenres.getOrNull(2)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TopGenresLightPink)
            .padding(bottom = 20.dp, top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.book_court_logo),
            contentDescription = "Book court logo"
        )
        Text(
            text = "Любимые жанры",
            fontFamily = Inter,
            fontWeight = FontWeight.Black,
            color = Color.Black,
            fontSize = 32.sp
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            userScrollEnabled = false
        ) {
            items(top3Genres) { item ->
                item?.let {
                    GenreItem(
                        genre = it.first,
                        booksAmount = it.second
                    )
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.open_book),
            contentDescription = "open book image",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentScale = ContentScale.FillBounds
        )
        ShareStatisticsButton(
            context = context,
            contentView = view,
            modifier = Modifier.weight(1f,false)
        )
    }

}

@Composable
private fun GenreItem(
    genre: String,
    booksAmount: Int
) {
    val string = if (booksAmount == 1) "книга" else if (booksAmount in 2..4) "книги" else "книг"
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Blue)
            .height(100.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = genre,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                fontFamily = Inter,
                fontWeight = FontWeight.Black,
                color = Color.White,
                fontSize = 16.sp
            )
            Text(
                text = "$booksAmount $string",
                modifier = Modifier.padding(start = 16.dp),
                fontFamily = Inter,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ReadBooksStats(
    mViewModel: StatisticsViewModel = hiltViewModel()
) {
    val booksAmount = mViewModel.readBooks.value?.size
    val view = LocalView.current
    val context = LocalContext.current
    val string = if (booksAmount == 1) "книга" else if (booksAmount in 2..4) "книги" else "книг"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterPinkBackground)
            .padding(bottom = 20.dp, top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        Image(
            painter = painterResource(id = R.drawable.book_court_logo),
            contentDescription ="Book court logo"
        )
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

        Image(
            painter = painterResource(id = R.drawable.cup_coffee_open_book),
            contentDescription = "lyuteratura logo",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            contentScale = ContentScale.Fit
        )
        ShareStatisticsButton(
            context = context,
            contentView = view,
            modifier = Modifier.weight(1f,false)
        )
    }

}

@Composable
fun FavoriteAuthors(
    mViewModel: StatisticsViewModel = hiltViewModel()
) {
    val view = LocalView.current
    val context = LocalContext.current
    val topAuthors = mViewModel.getTopAuthors().toList()
    val firstPlaceAuthor = topAuthors.getOrNull(0)?.first
    val secondPlaceAuthor = topAuthors.getOrNull(1)?.first
    val thirdPlaceAuthor = topAuthors.getOrNull(2)?.first
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TopAuthorsLightPink)
            .padding(bottom = 20.dp, top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.book_court_logo),
            contentDescription = "Book court logo"
        )
        Text(
            text = "Ваш ТОП - 3 авторов",
            fontFamily = Inter,
            fontWeight = FontWeight.Black,
            color = Color.Black,
            fontSize = 32.sp
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                firstPlaceAuthor?.let { author ->
                    val authorBook = mViewModel.user.value?.readBooksList?.find { book ->
                        book.bookInfo.author == author
                    }
                    authorBook?.let {
                        TopAuthorItem(
                            book = it,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                secondPlaceAuthor?.let { author ->
                    val authorBook = mViewModel.user.value?.readBooksList?.find { book ->
                        book.bookInfo.author == author
                    }
                    authorBook?.let {
                        TopAuthorItem(
                            book = it,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }

                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                thirdPlaceAuthor?.let { author ->
                    val authorBook = mViewModel.user.value?.readBooksList?.find { book ->
                        book.bookInfo.author == author
                    }
                    authorBook?.let {
                        TopAuthorItem(
                            book = it,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
            }
        }
        ShareStatisticsButton(
            context = context,
            contentView = view,
            modifier = Modifier.weight(1f,false)
        )
    }
}

@Composable
fun TopAuthorItem(
    book: Book,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        SubcomposeAsyncImage(
            model = book.bookInfo.image,
            contentDescription = "${book.bookInfo.author}'s book image",
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.35f)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.FillBounds,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .scale(0.5f)
                )
            }
        )
        Column {
            Text(
                text = book.bookInfo.author,
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 18.sp
            )
            Text(
                text = book.bookInfo.genre,
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun StoryLikePages(
    pagerState: PagerState,
    screens: List<String>
) {
    HorizontalPager(state = pagerState, dragEnabled = false) { page ->
        when (screens[page]) {
            "IgraSlov" -> {
                PartnerIgraSlov()
            }
            "Lyuteratura" -> {
                PartnerLyuteratura()
            }
            "ReadBooks" -> {
                ReadBooksStats()
            }
            "FavoriteAuthors" -> {
                FavoriteAuthors()
            }
            "FavoriteGenres" -> {
                FavoriteGenresStats()
            }
            else -> {
                PartnerZarya()
            }
        }
    }
}