import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bookcourt.R
import com.example.bookcourt.ui.statistics.StatisticsViewModel
import com.example.bookcourt.ui.theme.Brown
import com.example.bookcourt.ui.theme.CustomButton
import com.example.bookcourt.ui.theme.Gilroy
import com.example.bookcourt.utils.BottomBarScreen


@Composable
fun StatisticsScreen(navController: NavController, mViewModel: StatisticsViewModel) {
    var state = remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        StatisticsScreenUi(navController, state, mViewModel)
    }
}

@Composable
fun StatisticsScreenUi(
    navController: NavController,
    state: MutableState<Boolean>,
    mViewModel: StatisticsViewModel
) {
    val user = mViewModel.user
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        val userId =
            "b9786ae1-4efb-46c4-a493-cb948cb80103" // Так как нет авторизации я храню просто id пользователя в тестовом формате
        mViewModel.getUserData(context)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .clip(RoundedCornerShape(60.dp))
            .clickable { state.value = !state.value }
    ) {
        Image(
            painter = painterResource(id = R.drawable.book_amount_card_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .padding(start = 40.dp, end = 40.dp, top = 40.dp, bottom = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ReadBooksCardHeader(navController, state, mViewModel)
            CustomButton("Поделиться!") {
                mViewModel.user.value?.let {
                    mViewModel.shareStats(context)
                }
            }
        }

    }
}

@Composable
fun ReadBooksCardHeader(
    navController: NavController,
    state: MutableState<Boolean>,
    mViewModel: StatisticsViewModel
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (state.value) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(color = Color.White)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(color = Brown)
                )
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(color = Brown)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(color = Color.White)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            Box() {}
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Lead App Icon",
                modifier = Modifier.size(120.dp)
            )
            Row(horizontalArrangement = Arrangement.End) {
                Icon(
                    painter = painterResource(id = R.drawable.close_square),
                    contentDescription = "",
                    tint = Brown,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable {
                            navController.popBackStack()
                            navController.navigate(route = BottomBarScreen.Recomendations.route)
                        }
                )
            }
        }
        if (state.value) {
            Text(
                text = "BookCourt помогает вам находить новые шедевры литературы.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = Gilroy
            )
            Text(
                text = "Столько книг вы прочитали за месяц.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = Gilroy
            )
            Spacer(modifier = Modifier.height(22.dp))
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Уже ${mViewModel.user.value?.statistics?.numberOfReadBooks}!",
                    fontSize = 32.sp,
                    fontFamily = Gilroy,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Text(
                text = "Кажется, вы не боитесь открывать для себя новые жанры!",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = Gilroy
            )
            Text(
                text = "За месяц эти жанры понравились вам больше всего",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontFamily = Gilroy
            )
        }
        Spacer(modifier = Modifier.height(22.dp))
        DisplayBooks(state, mViewModel)
    }
}

@Composable
fun DisplayBooks(state: MutableState<Boolean>, mViewModel: StatisticsViewModel) {
    if (state.value) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.one_books),
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .rotate(-38f)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.wrapContentWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.three_books),
                    contentDescription = "",
                    modifier = Modifier.size(80.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.three_books),
                    contentDescription = "",
                    modifier = Modifier
                        .size(80.dp)
                        .rotate(180f)
                )
                Image(
                    painter = painterResource(id = R.drawable.three_books),
                    contentDescription = "",
                    modifier = Modifier.size(80.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.three_books),
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .rotate(180f)
            )
        }
    } else {
        val genres = mViewModel.user.value?.statistics?.favoriteGenreList
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Column() {
                Text(
                    text = genres?.get(0) ?: "",
                    fontFamily = Gilroy,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.three_books),
                    contentDescription = "",
                    modifier = Modifier.size(80.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.three_books),
                    contentDescription = "",
                    modifier = Modifier.size(80.dp)
                )
            }
            Column() {

                Text(
                    text = genres?.get(1) ?: "",
                    fontFamily = Gilroy,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.three_books),
                    contentDescription = "",
                    modifier = Modifier.size(80.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.three_books),
                    contentDescription = "",
                    modifier = Modifier
                        .size(80.dp)
                        .rotate(180f)
                )
                Image(
                    painter = painterResource(id = R.drawable.three_books),
                    contentDescription = "",
                    modifier = Modifier.size(80.dp)
                )
            }
            Column() {
                Text(
                    text = genres?.get(2) ?: "",
                    fontFamily = Gilroy,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.three_books),
                    contentDescription = "",
                    modifier = Modifier.size(80.dp)
                )
            }
        }
    }

}

