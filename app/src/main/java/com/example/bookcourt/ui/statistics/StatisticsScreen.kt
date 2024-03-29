package com.example.bookcourt.ui.statistics

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.Constants
import com.example.bookcourt.utils.Constants.LIMIT_WINDOW_HEIGHT
import com.example.bookcourt.utils.ShareStatisticsButton
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun Statistics(
    onNavigateToRecommendation: () -> Unit,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val windowHeight =
        LocalConfiguration.current.screenHeightDp.toFloat() * LocalDensity.current.density
    val bottomPadding = if (windowHeight > LIMIT_WINDOW_HEIGHT) 76.dp else 56.dp
    LaunchedEffect(key1 = Unit) {
        viewModel.getUserStats()
//        viewModel.getTopAuthors()
//        viewModel.getTopAuthors()
    }

    val targetOffset = 500
    val pagerState = rememberPagerState(pageCount = Constants.statisticScreensList.size)
    val coroutineScope = rememberCoroutineScope()

    var currentPage by remember {
        mutableStateOf(0)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        coroutineScope.launch {
                            currentPage = if (offset.x < targetOffset) {
                                if (currentPage == 0) {
                                    0
                                } else {
                                    currentPage - 1
                                }
                            } else {
                                currentPage + 1
                            }
                            if (currentPage == pagerState.pageCount) {
                                viewModel.metricScreenTime()
                                onNavigateToRecommendation()
                            } else {
                                pagerState.animateScrollToPage(page = currentPage % pagerState.pageCount)
                            }
                        }
                    }
                )
            }

    ) {
        StatisticsPages(
            bottomPadding = bottomPadding,
            pagerState = pagerState,
            Constants.statisticScreensList
        )
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
                    .zIndex(3f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.padding(4.dp))
                for (i in 0 until pagerState.pageCount) {
                    StoriesProgressBar(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp)),
                        startProgress = (i == currentPage),
                        isNext = (i > currentPage)
                    ) {
                        coroutineScope.launch {
                            if (currentPage == pagerState.pageCount - 1) {
                                viewModel.metricScreenTime()
                                onNavigateToRecommendation()
                            } else {
                                currentPage = (currentPage + 1) % (pagerState.pageCount)
                                pagerState.animateScrollToPage(page = currentPage)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}

@Composable
fun FavoriteGenresStats(
    bottomPadding: Dp,
    mViewModel: StatisticsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val topGenres = mViewModel.favGenresList.toList()
    val top3Genres = listOf(
        topGenres.getOrNull(0),
        topGenres.getOrNull(1),
        topGenres.getOrNull(2)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TopGenresLightPink)
            .padding(bottom = bottomPadding, top = 50.dp),
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
            statisticsText = mViewModel.shareStatistics(),
            context = context,
            modifier = Modifier.weight(1f, false)
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
    bottomPadding: Dp,
    mViewModel: StatisticsViewModel = hiltViewModel()
) {
    val booksAmount = mViewModel.readBooks.value?.size
    val context = LocalContext.current
    val string = if (booksAmount == 1) "книга" else if (booksAmount in 2..4) "книги" else "книг"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LighterPinkBackground)
            .padding(bottom = bottomPadding, top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        Image(
            painter = painterResource(id = R.drawable.book_court_logo),
            contentDescription = "Book court logo"
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
            contentDescription = "cup coffee open book image",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            contentScale = ContentScale.Fit
        )
        ShareStatisticsButton(
            statisticsText = mViewModel.shareStatistics(),
            context = context,
            modifier = Modifier.weight(1f, false)
        )
    }
}

@Composable
fun FavoriteAuthors(
    bottomPadding: Dp,
    mViewModel: StatisticsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val topAuthors = mViewModel.favAuthorsList.toList()
    val firstPlaceAuthor = topAuthors.getOrNull(0)?.first
    val secondPlaceAuthor = topAuthors.getOrNull(1)?.first
    val thirdPlaceAuthor = topAuthors.getOrNull(2)?.first
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TopAuthorsLightPink)
            .padding(bottom = bottomPadding, top = 50.dp),
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
                    val authorBook =
                        mViewModel.user.value?.readBooksList?.find { book ->
                            book.bookInfo.author == author
                        }
                    authorBook?.let {
                        TopAuthorItem(
                            book = it
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
                    val authorBook =
                        mViewModel.user.value?.readBooksList?.find { book ->
                            book.bookInfo.author == author
                        }
                    authorBook?.let {
                        TopAuthorItem(
                            book = it
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
                    val authorBook =
                        mViewModel.user.value?.readBooksList?.find { book ->
                            book.bookInfo.author == author
                        }
                    authorBook?.let {
                        TopAuthorItem(
                            book = it
                        )
                    }
                }
            }
        }
        ShareStatisticsButton(
            statisticsText = mViewModel.shareStatistics(),
            context = context,
            modifier = Modifier.weight(1f, false)
        )
    }
}

@Composable
fun TopAuthorItem(
    book: Book,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxSize()
    ) {
        SubcomposeAsyncImage(
            model = book.bookInfo.image,
            contentDescription = "${book.bookInfo.author}'s book image",
            modifier = Modifier
                .fillMaxHeight()
                //.fillMaxWidth(0.3f)
                .width(115.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(15.dp)),
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
fun StatisticsPages(
    bottomPadding: Dp,
    pagerState: PagerState,
    screens: List<String>
) {
    HorizontalPager(state = pagerState, dragEnabled = false) { page ->
        when (screens[page]) {
            "IgraSlov" -> {
                PartnerIgraSlov(bottomPadding = bottomPadding)
            }
            "Lyuteratura" -> {
                PartnerLyuteratura(bottomPadding = bottomPadding)
            }
            "ReadBooks" -> {
                ReadBooksStats(bottomPadding = bottomPadding)
            }
            "FavoriteAuthors" -> {
                FavoriteAuthors(bottomPadding = bottomPadding)
            }
            "FavoriteGenres" -> {
                FavoriteGenresStats(bottomPadding = bottomPadding)
            }
            else -> {
                PartnerZarya(bottomPadding = bottomPadding)
            }
        }
    }
}

@Composable
fun StoriesProgressBar(
    modifier: Modifier,
    startProgress: Boolean = false,
    isNext: Boolean,
    onAnimationEnd: () -> Unit
) {
    val percent = remember { Animatable(0f) }

    if (startProgress) {
        LaunchedEffect(Unit) {
            percent.snapTo(0f)
            percent.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = (5000 * (1f - percent.value)).toInt(),
                    easing = LinearEasing
                )
            )
            onAnimationEnd()
        }
    } else {
        LaunchedEffect(Unit) {
            if (isNext) {
                percent.snapTo(0f)
            } else {
                percent.snapTo(1f)
            }
        }
    }
    LinearProgressIndicator(
        backgroundColor = InactiveProgressGrey,
        color = ActiveProgressGrey,
        modifier = modifier,
        progress = percent.value
    )
}
