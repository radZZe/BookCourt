package com.example.bookcourt.ui.statistics

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.statistics.StatisticsScreenRequest.AMOUNT_OF_BOOKS
import com.example.bookcourt.ui.statistics.StatisticsScreenRequest.FAVORITE_AUTHORS
import com.example.bookcourt.ui.statistics.StatisticsScreenRequest.FAVORITE_GENRES
import com.example.bookcourt.ui.statistics.StatisticsScreenRequest.PARTNER
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.Buttons
import com.example.bookcourt.utils.Partners
import com.example.bookcourt.utils.Screens


@Composable
fun SwipableStats(navController: NavController) {
    
}

@Composable
fun Statistics(navController: NavController, mViewModel: StatisticsViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = Unit) {
        mViewModel.getUserStats()
    }
    when (mViewModel.currentScreen.value) {
        AMOUNT_OF_BOOKS -> {
            ReadBooksStats(navController = navController)
        }
        FAVORITE_AUTHORS -> {
            FavoriteAuthors(navController = navController)
        }
        PARTNER -> {
           PartnerLyuteratura(navController = navController)
        }
        else -> {
            FavoriteGenresStats(navController = navController)
        }
    }
}

@Preview
@Composable
private fun PartnerLyuteraturaPreview() {
    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(LightPinkBackground)
            .clickable {

            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.partner_lyuteratura_logo),
                contentDescription = "lyuteratura logo",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.1f),
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(id = R.drawable.partner_lyuteratura_content),
                contentDescription = "lyuteratura content",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                ,
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Любите детей, книги и творчество?",
                fontFamily = Inter,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                fontSize = 32.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "Тогда не проходите мимо и загляните в детский книжный магазин “Лютература”.",
                fontFamily = Inter,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Button(
                onClick = {
                    val sendIntent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            Partners.lyuteraturaUrl
                        )
                    )
                    val webIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(webIntent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(60.dp))
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(LightYellowBtn)
            ) {
                Text(text = "Заглянуть в магазин")
            }
        }
    }
}

@Preview
@Composable
private fun ReadBooksStatsPreview() {
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
            Image(
                painter = painterResource(id = R.drawable.book_court_logo),
                contentDescription ="Book court logo"
            )
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
                    text =  "1. Увеличивает словарный запас\n" +
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
                contentDescription = "cup coffee and open book image",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f),
                contentScale = ContentScale.Fit
            )
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

@Preview
@Composable
fun FavoriteGenresStatsPreview() {
    val view = LocalView.current
    val context = LocalContext.current
    val top3Genres = listOf(
        Pair("Genre",3),
        Pair("Genre2",2),
        Pair("Genre1",1),
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clip(RoundedCornerShape(30.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TopGenresLightPink),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(id = R.drawable.book_court_logo),
                contentDescription ="Book court logo"
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
            ){
                items(top3Genres){ item->
                    item.let {
                        GenreItem(
                            genre = it.first,
                            booksAmount =it.second
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
        }
        Button(
            onClick = {
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(60.dp))
                .height(45.dp),
            colors = ButtonDefaults.buttonColors(LightYellowBtn)
        ) {
            Text(text = "Поделиться")
        }
    }
}

@Preview
@Composable
private fun FavoriteAuthorsPreview() {
    val view = LocalView.current
    val context = LocalContext.current
    val firstPlaceAuthor = "Author1"
    val secondPlaceAuthor ="Author2"
    val thirdPlaceAuthor = "Author3"
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clip(RoundedCornerShape(30.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TopAuthorsLightPink),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(id = R.drawable.book_court_logo),
                contentDescription ="Book court logo"
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
                    .weight(3f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAuthorItemPreview(
                    authorName  = firstPlaceAuthor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                TopAuthorItemPreview(
                    authorName  = secondPlaceAuthor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                TopAuthorItemPreview(
                    authorName  = thirdPlaceAuthor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            Button(
                onClick = {
                },
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
private fun TopAuthorItemPreview(
    authorName:String,
    modifier: Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
      Image(
            painter = painterResource(id = R.drawable.book_court_logo),
            contentDescription = "${authorName}'s book image",
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.35f)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.FillBounds
        )
        Column {
            Text(
                text = authorName,
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 18.sp
            )
            Text(
                text = "Genre",
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                fontSize = 16.sp)
        }
    }
}


@Composable
fun FavoriteGenresStats(
    navController: NavController,
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
                mViewModel.currentScreen.value = FAVORITE_GENRES
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TopGenresLightPink),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            TopBar(navController, FAVORITE_AUTHORS)
            Image(
                painter = painterResource(id = R.drawable.book_court_logo),
                contentDescription ="Book court logo"
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
            ){
                items(top3Genres){ item->
                    item?.let {
                        GenreItem(
                            genre = it.first,
                            booksAmount =it.second
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
        }
        Button(
            onClick = {
                mViewModel.shareStatistics(view,context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(60.dp))
                .height(45.dp),
            colors = ButtonDefaults.buttonColors(LightYellowBtn)
        ) {
            Text(text = "Поделиться")
        }
    }
}


@Composable
private fun GenreItem(
    genre:String,
    booksAmount:Int
){
    val string = if (booksAmount == 1) "книга" else if (booksAmount in 2..4) "книги" else "книг"
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Blue)
            .height(100.dp)
    ){
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
private fun PartnerLyuteratura(
    navController: NavController,
    mViewModel: StatisticsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(LightPinkBackground)
            .clickable {
                mViewModel.sendOnClickMetric(
                    DataClickMetric(
                        Buttons.SWAP_STAT,
                        Screens.Statistics.route
                    )
                )
                mViewModel.currentScreen.value = FAVORITE_AUTHORS
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(navController = navController, rq = PARTNER)

            Image(
                painter = painterResource(id = R.drawable.partner_lyuteratura_logo),
                contentDescription = "lyuteratura logo",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.1f),
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(id = R.drawable.partner_lyuteratura_content),
                contentDescription = "lyuteratura content",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                ,
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Любите детей, книги и творчество?",
                fontFamily = Inter,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                fontSize = 32.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "Тогда не проходите мимо и загляните в детский книжный магазин “Лютература”.",
                fontFamily = Inter,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Button(
                onClick = {
                    val sendIntent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            Partners.lyuteraturaUrl
                        )
                    )
                    val webIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(webIntent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(60.dp))
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(LightYellowBtn)
            ) {
                Text(text = "Заглянуть в магазин")
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
    val view = LocalView.current
    val context = LocalContext.current
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
            TopBar(navController, AMOUNT_OF_BOOKS)
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
                    text =  "1. Увеличивает словарный запас\n" +
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
            Button(
                onClick = {mViewModel.shareStatistics(view, context) },
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
private fun FavoriteAuthors(
    navController: NavController,
    mViewModel: StatisticsViewModel = hiltViewModel()
) {
    val view = LocalView.current
    val context = LocalContext.current
    val topAuthors = mViewModel.getTopAuthors().toList()
    val firstPlaceAuthor = topAuthors.getOrNull(0)?.first
    val secondPlaceAuthor =topAuthors.getOrNull(1)?.first
    val thirdPlaceAuthor = topAuthors.getOrNull(2)?.first
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
                mViewModel.currentScreen.value = FAVORITE_GENRES
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TopAuthorsLightPink),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            TopBar(navController, FAVORITE_AUTHORS)
            Image(
                painter = painterResource(id = R.drawable.book_court_logo),
                contentDescription ="Book court logo"
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
                   .weight(3f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                firstPlaceAuthor?.let { author->
                    val authorBook = mViewModel.user.value?.readBooksList?.find { book->
                        book.bookInfo.author==author
                    }
                    authorBook?.let {
                        TopAuthorItem(
                            book = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    }
                }
                secondPlaceAuthor?.let { author->
                    val authorBook = mViewModel.user.value?.readBooksList?.find { book->
                        book.bookInfo.author==author
                    }
                    authorBook?.let {
                        TopAuthorItem(
                        book = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                        )
                    }

                }
                thirdPlaceAuthor?.let{ author->
                    val authorBook = mViewModel.user.value?.readBooksList?.find { book->
                        book.bookInfo.author==author
                    }
                    authorBook?.let {
                        TopAuthorItem(
                            book = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    }
                }
            }
            Button(
                onClick = {
                    mViewModel.shareStatistics(view,context)
                },
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
private fun TopAuthorItem(
    book: Book,
    modifier: Modifier
){
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
                fontSize = 16.sp)
        }
    }
}



@Composable
private fun TopBar(
    navController: NavController,
    rq: String,
    mViewModel: StatisticsViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        when (rq) {
            AMOUNT_OF_BOOKS -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Grey)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                    )
                }
            }
            FAVORITE_AUTHORS -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Grey)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                    )
                }
            }
            PARTNER -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Grey)
                    )
                }
            }
            else -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Grey)
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 30.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.close_square),
                contentDescription = "Close cross",
                tint = Color.White,
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        mViewModel.sendOnClickMetric(
                            DataClickMetric(
                                Buttons.CLOSE,
                                Screens.Statistics.route
                            )
                        )
                        mViewModel.metricScreenTime()
                        navController.popBackStack()
                        navController.navigate(route = Screens.Recommendation.route)
                    }

            )
        }
    }
}

@Composable
private fun BookShelf(booksAmount: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.BottomStart
    ) {
        DrawShelf(
            modifier = Modifier
                .padding(end = 40.dp)
                .fillMaxWidth()
                .height(30.dp)
        )
        DisplayBooks(books = booksAmount, modifier = Modifier.padding(bottom = 10.dp))
    }
}

@Composable
private fun BookShelves(booksAmount: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.BottomStart
    ) {
        LongDeck(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        )
        if (booksAmount >= 12) {
            DisplayBooks(books = 12, modifier = Modifier.padding(bottom = 10.dp))
        } else {
            DisplayBooks(books = booksAmount, modifier = Modifier.padding(bottom = 10.dp))
        }
    }
}

@Composable
private fun ShareApp(textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, start = 40.dp, end = 40.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
//        Icon(
//            painter = painterResource(id = R.drawable.ic_share),
//            contentDescription = "Share",
//            tint = Color.White,
//            modifier = Modifier
//                .size(30.dp)
//                .alpha(0f)
//        )
        Text(
            text = "BookCourt",
            color = textColor,
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )
//        Icon(
//            painter = painterResource(id = R.drawable.ic_share),
//            contentDescription = "Share",
//            tint = Color.White,
//            modifier = Modifier.size(30.dp)
//        )
    }
}

@Composable
private fun DisplayBooks(books: Int, modifier: Modifier) {
    val colors = listOf<List<Color>>(
        listOf(Color(0xFF89905A), Color(0xFF2E3527)),
        listOf(Color(0xFFE39C64), Color(0xFF6E3B14)),
        listOf(Color(0xFF9C4C43), Color(0xFF351C18)),
        listOf(Color(0xFF004963), Color(0xFF233237)),
        listOf(Color(0xFF89905A), Color(0xFF2E3527)),
        listOf(Color(0xFFE39C64), Color(0xFF6E3B14)),
        listOf(Color(0xFF9C4C43), Color(0xFF351C18)),
        listOf(Color(0xFF004963), Color(0xFF233237)),
        listOf(Color(0xFF89905A), Color(0xFF2E3527)),
        listOf(Color(0xFFE39C64), Color(0xFF6E3B14)),
        listOf(Color(0xFF9C4C43), Color(0xFF351C18)),
        listOf(Color(0xFF004963), Color(0xFF233237)),
        listOf(Color(0xFF89905A), Color(0xFF2E3527)),
        listOf(Color(0xFFE39C64), Color(0xFF6E3B14)),
        listOf(Color(0xFF9C4C43), Color(0xFF351C18)),
        listOf(Color(0xFF004963), Color(0xFF233237)),
        listOf(Color(0xFF89905A), Color(0xFF2E3527)),
        listOf(Color(0xFFE39C64), Color(0xFF6E3B14)),
        listOf(Color(0xFF9C4C43), Color(0xFF351C18)),
        listOf(Color(0xFF004963), Color(0xFF233237)),
        listOf(Color(0xFF89905A), Color(0xFF2E3527)),
        listOf(Color(0xFFE39C64), Color(0xFF6E3B14)),
        listOf(Color(0xFF9C4C43), Color(0xFF351C18)),
        listOf(Color(0xFF004963), Color(0xFF233237)),
        listOf(Color(0xFF89905A), Color(0xFF2E3527)),
        listOf(Color(0xFFE39C64), Color(0xFF6E3B14))
    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        for (i in 1..books) {
            DrawBook(
                modifier = Modifier
                    .height(80.dp)
                    .width(30.dp),
                color = colors[i][0],
                shadowColor = colors[i][1]
            )
        }
    }
}

@Composable
private fun DrawBook(modifier: Modifier, color: Color, shadowColor: Color) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val bookWidth = width / 5 * 3
            val bookHeight = height / 8 * 7
            val bookHeight3DPart = height - bookHeight
            val bookWidth3DPart = (width - bookWidth) * (height * 0.009f)

            var path = Path().apply {
                moveTo(0f, height)
                lineTo(bookWidth, height)
                lineTo(bookWidth, height - bookHeight)
                lineTo(0f, height - bookHeight)
                close()
            }
            drawPath(
                path,
                color = color
            ) // frontView

            path = Path().apply {
                moveTo(bookWidth, height - bookHeight)
                lineTo(bookWidth3DPart + bookWidth, 0f)
                lineTo(bookWidth3DPart + bookWidth, bookHeight)
                lineTo(bookWidth, height)
                close()
            }
            drawPath(
                path,
                color = shadowColor
            ) // front wall

            path = Path().apply {
                moveTo(0f, bookHeight3DPart)
                lineTo(bookWidth, bookHeight3DPart)
                lineTo(bookWidth + bookWidth3DPart, 0f)
                lineTo(bookWidth3DPart, 0f)
                close()
            }
            drawPath(
                path,
                color = Color.White
            ) // top

            // shadow
            path = Path().apply {
                moveTo(bookWidth, height)
                lineTo(width * 3, bookHeight)
                lineTo(bookWidth + bookWidth3DPart, bookHeight)
                close()
            }
            drawPath(
                path,
                color = DarkStatShelf
            )
        }
    }

}

@Composable
private fun DrawShelf(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val shelfWidth = width * 5 / 6

            var path = Path().apply {
                moveTo(0f, height)
                lineTo(shelfWidth, height)
                lineTo(width, 0f)
                lineTo(0f, 0f)
                close()
            }
            drawPath(
                path,
                color = StatShelf
            )

            path = Path().apply {
                moveTo(width, 0f)
                lineTo(shelfWidth, height)
                lineTo(shelfWidth, height * 3)
                lineTo(width, height * 3 - height)
                close()
            }
            drawPath(
                path,
                color = DarkStatShelf
            )
        }

    }
}

@Composable
private fun ShelfDeck(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val shelfWidth = width * 5 / 6

            var path = Path().apply {
                moveTo(0f, height)
                lineTo(width, height)
                lineTo(width, 0f)
                lineTo(width - shelfWidth, 0f)
                close()
            }
            drawPath(
                path,
                color = Color(0xFFBBA495)
            )

            path = Path().apply {
                moveTo(0f, height)
                lineTo(0f, height + height / 2)
                lineTo(width, height + height / 2)
                lineTo(width, height)
                close()
            }
            drawPath(
                path,
                color = Color(0xFF9E887A)
            )
        }
    }
}

@Composable
private fun ShelfWall(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val wallWidth = width / 3
            val wallHeight = height / 6 * 4
            val wall3DHeight = height - wallHeight

            var path = Path().apply {
                moveTo(0f, wall3DHeight)
                lineTo(0f, height)
                lineTo(wallWidth, height)
                lineTo(wallWidth, wall3DHeight)
                close()
            }
            drawPath(
                path,
                color = Color(0xFF876E5E)
            )

            path = Path().apply {
                moveTo(wallWidth, wall3DHeight)
                lineTo(wallWidth, height)
                lineTo(width, wallHeight + 96)
                lineTo(width, 104f)
                close()
            }
            drawPath(
                path,
                color = Color(0xFF6D584B)
            )
        }
    }
}

@Composable
private fun DisplayGenresShelves(mViewModel: StatisticsViewModel) {
    val genresMap = mViewModel.getTopGenres()
    var top1Genre = ""
    var top1Books = 0
    var top2Genre = ""
    var top2Books = 0
    var top3Genre = ""
    var top3Books = 0
    if (genresMap.keys.toList().size >= 3) {
        top1Genre = genresMap.keys.toList()[0]
        top1Books = genresMap.values.toList()[0]
        top2Genre = genresMap.keys.toList()[1]
        top2Books = genresMap.values.toList()[1]
        top3Genre = genresMap.keys.toList()[2]
        top3Books = genresMap.values.toList()[2]
    } else if (genresMap.keys.toList().size >= 2) {
        top1Genre = genresMap.keys.toList()[0]
        top1Books = genresMap.values.toList()[0]
        top2Genre = genresMap.keys.toList()[1]
        top2Books = genresMap.values.toList()[1]
    } else if (genresMap.keys.toList().isNotEmpty()) {
        top1Genre = genresMap.keys.toList()[0]
        top1Books = genresMap.values.toList()[0]
    } else {
        top1Genre = "Пусто..."
    }
    if (top1Books >= 10) {
        top1Books = 9
    }
    if (top2Books >= 10) {
        top2Books = 9
    }
    if (top3Books >= 10) {
        top3Books = 9
    }
    Box(
        modifier = Modifier.wrapContentSize(),
        contentAlignment = Alignment.BottomStart
    ) {
        ShelfDeck(
            modifier = Modifier
                .padding(start = 40.dp)
                .fillMaxWidth()
                .height(30.dp)
        )
        ShelfWall(
            modifier = Modifier
                .padding(start = 40.dp)
                .width(60.dp)
                .height(200.dp)
        )
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxWidth()
        ) {
            DisplayBooks(books = top1Books, modifier = Modifier.padding(bottom = 6.dp))
            Text(
                text = top1Genre,
                fontFamily = Manrope,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }


        ShelfDeck(
            modifier = Modifier
                .padding(start = 40.dp, bottom = 140.dp)
                .fillMaxWidth()
                .height(30.dp)
        )
        ShelfWall(
            modifier = Modifier
                .padding(start = 40.dp, bottom = 140.dp)
                .width(60.dp)
                .height(200.dp)
        )
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxWidth()
        ) {
            DisplayBooks(books = top2Books, modifier = Modifier.padding(bottom = 146.dp))
            Text(
                text = top2Genre,
                fontFamily = Manrope,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 150.dp)
            )
        }

        ShelfDeck(
            modifier = Modifier
                .padding(start = 40.dp, bottom = 280.dp)
                .fillMaxWidth()
                .height(30.dp)
        )
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxWidth()
        ) {
            DisplayBooks(books = top3Books, modifier = Modifier.padding(bottom = 286.dp))
            Text(
                text = top3Genre,
                fontFamily = Manrope,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 290.dp)
            )
        }
    }
}

@Composable
private fun LongDeck(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(0f, height)
                lineTo(width, height)
                lineTo(width, 0f)
                close()
            }
            drawPath(
                path,
                color = StatShelf
            )
        }
    }
}

object StatisticsScreenRequest {
    const val AMOUNT_OF_BOOKS = "AMOUNT_OF_BOOKS_RQ"
    const val PARTNER = "partner lyuteratura"
    const val FAVORITE_GENRES = "FAVORITE_GENRES_RQ"
    const val FAVORITE_AUTHORS = "FAVORITE_AUTHORS_RQ"
}

