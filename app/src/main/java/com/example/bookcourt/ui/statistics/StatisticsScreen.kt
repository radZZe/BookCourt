import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bookcourt.R
import com.example.bookcourt.models.FavoriteGenres
import com.example.bookcourt.ui.statistics.StatisticsViewModel
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.BottomBarScreen



@Composable
fun StatisticsScreen(navController: NavController, mViewModel: StatisticsViewModel) {
//    val currentScreen = mutableStateOf(mViewModel.currentScreen.value)
    ReadBooksStat(navController = navController, mViewModel = mViewModel)
}

@Composable
fun FavoriteGenresStat(navController: NavController, mViewModel: StatisticsViewModel) {
    TopBar(navController = navController)

}

@Composable
fun ReadBooksStat(navController: NavController, mViewModel: StatisticsViewModel) {
    val booksAmount = mViewModel.booksAmount.collectAsState(initial = "").value.split(" ").size
    val string = if(booksAmount == 1) "книга" else if (booksAmount in 2..4) "книги" else "книг"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundGrey),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopBar(navController)
        Text(
            text = "Посмотрим,\nсколько уже \nпрочитано...",
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            color = TextBrown,
            fontSize = 32.sp,
            modifier = Modifier.padding(start = 45.dp)
        )
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 80.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$booksAmount $string!",
                    fontFamily = Manrope,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 50.sp
                )
                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .fillMaxWidth()
                        .background(SemiLightBrown)
                )
            }
        }
        BookShelf(booksAmount)
        ShareApp()
    }
}

@Composable
fun TopBar(navController: NavController) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
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
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 30.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.close_square),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        navController.popBackStack()
                        navController.navigate(route = BottomBarScreen.Recomendations.route)
                    }
            )
        }
    }
}

@Composable
fun BookShelf(booksAmount: Int) {
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
fun ShareApp() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, start = 40.dp, end = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_share),
            contentDescription = "Share",
            tint = Color.White,
            modifier = Modifier
                .size(30.dp)
                .alpha(0f)
        )
        Text(
            text = "BookCourt",
            color = TextBrown,
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_share),
            contentDescription = "Share",
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun DisplayBooks(books: Int, modifier: Modifier) {
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
        listOf(Color(0xFFE39C64), Color(0xFF6E3B14)),
        listOf(Color(0xFF9C4C43), Color(0xFF351C18)),
        listOf(Color(0xFF004963), Color(0xFF233237)),

    )
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
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
//        DrawShadow(
//            modifier = Modifier
//                .height(80.dp)
//                .width(30.dp)
//        )
    }
}

@Composable
fun DrawBook(modifier: Modifier, color: Color, shadowColor: Color) {
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
fun DrawShelf(modifier: Modifier) {
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
fun ShelfDeck(modifier: Modifier) {
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
                color = StatShelf
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
                color = DarkStatShelf
            )
        }
    }
}

@Composable
fun ShelfWall(modifier: Modifier) {
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
                lineTo(6f, height)
                lineTo(6f, 0f)
            }
        }
    }
}

@Preview
@Composable
fun ShelfDeckPreview() {
    Column(modifier=  Modifier.fillMaxSize()) {
        ShelfDeck(modifier = Modifier
            .padding(start = 40.dp)
            .fillMaxWidth()
            .height(30.dp))
    }
}


object StatisticsScreenRequest {
    val AMOUNT_OF_BOOKS = "AMOUNT_OF_BOOKS_RQ"
    val FAVORITE_GENRES = "FAVORITE_GENRES_RQ"
    val FAVORITE_AUTHORS = "FAVORITE_AUTHORS_RQ"
}

