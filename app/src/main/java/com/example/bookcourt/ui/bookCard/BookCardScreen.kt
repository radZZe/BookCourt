package com.example.bookcourt.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.book.BookInfo
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.bookCard.BookCardViewModel
import com.example.bookcourt.ui.recommendation.BookCardImage
import com.example.bookcourt.ui.recommendation.CategoriesBlock
import com.example.bookcourt.ui.recommendation.FeedbackBlock
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.utils.Buttons
import com.example.bookcourt.utils.Constants
import com.example.bookcourt.utils.Screens
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun BookCardScreen(
    bookId: String,
    onNavigateBack:()->Unit,
    onNavigateLeaveFeedback:(title:String,rate:Int)->Unit,
    onNavigateListFeedbacks:(title:String)->Unit,
    viewModel:BookCardViewModel = hiltViewModel(),
    feedbackText:String? = null,
    needToUpdate:Boolean = false,
    newRate:Int?=null){
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        viewModel.getBookByID(context,bookId)
    }
    val book = viewModel.book.value
    if(needToUpdate && newRate!=null){
        book!!.bookInfo.rate = newRate.toFloat()
    }
    if(book != null){
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                MainBgColor
            ))
        {
            val windowHeight =
                LocalConfiguration.current.screenHeightDp.toFloat() * LocalDensity.current.density
            BookCardTopBar({onNavigateBack()},R.drawable.igra_slov_logo)
            Spacer(modifier = Modifier.height(18.dp))
            Box(Modifier.fillMaxWidth().height(if (windowHeight > Constants.LIMIT_WINDOW_HEIGHT) 340.dp else 300.dp)){
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(23.dp))
                    .fillMaxWidth(0.5f)
                    .align(
                        Alignment.Center
                    )){
                    CardOfBook(book.bookInfo.image)


                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            BookCardMainContent(feedbackText,needToUpdate,book,onNavigateLeaveFeedback,onNavigateListFeedbacks,viewModel.rate.value){
                viewModel.addBasketItem(
                    BasketItem(
                        data = book
                    )
                )
            }

        }
    }else{
        Box(){

        }
    }


}

@Composable
fun BookCardTopBar(
    onBackNavigate:()->Unit,
    imageLogo:Int,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    ) {
        Row(modifier = Modifier.align(Alignment.CenterStart)) {
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier.clickable(interactionSource =  MutableInteractionSource(),
                    indication = null) {
                    onBackNavigate()

                }
            )
        }
        Image(painter = painterResource(id = imageLogo), contentDescription = null,modifier = Modifier
            .align(
                Alignment.Center
            )
            .size(45.dp))
        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
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
        }
    }
}

@Composable
fun CardOfBook(uri:String){
    BookCardImage(uri = uri,true)

}

@Composable
fun BookCardMainContent(
    feedbackText: String?,
    needToUpdate: Boolean,
    book:Book,
    onNavigateLeaveFeedback:(title:String,rate:Int)->Unit,
    onNavigateListFeedbacks:(title:String)->Unit,
    rate:Int,
    onClickAddButton: ()->Unit,
){
    Column(
        modifier = Modifier
            .background(MainBgColor),

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
                text = book.bookInfo.title,
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
                    text = "${book.bookInfo.author} | ${book.bookInfo.genre}",
                    color = Color(134, 134, 134),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.roboto_regular,
                        )
                    )
                )


            }
        }
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp))
                .background(
                    Color.White
                )
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 12.dp,
                    bottom = 10.dp
                ),
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
                        text = book.bookInfo.rate.toString(),
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
            val formatter = DateTimeFormatter.ofPattern("dd.MMMM.yyyy")
            val current = LocalDateTime.now().format(formatter)
            FeedbackBlock(
                username = "username",
                date = current,
                title = book.bookInfo.title,
                description = feedbackText ?: "",
                rate = book.bookInfo.rate.toInt(),
                leaveFeedbackVisibility = !needToUpdate,
                onNavigateToFeedback = {s-> onNavigateListFeedbacks(s)},
                onClickRatingBar = {
                        onNavigateLeaveFeedback(book.bookInfo.title,it)
                },
                disableLeaveFeedbackVisibility = {

                },
                frontItem = book,
                isNeedToUpdateFeedback = needToUpdate

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
            Box(modifier = Modifier.fillMaxWidth()){
                Column(modifier = Modifier
                    .height(134.dp)
                    .align(Alignment.TopStart)) {
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
                        text = book.bookInfo.description,
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
            }

            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Column() {
                    Text(
                        text = "Автор",
                        color = Color(78, 78, 78),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "Год издания",
                        color = Color(78, 78, 78),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "Издательство",
                        color = Color(78, 78, 78),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "Тип обложки",
                        color = Color(78, 78, 78),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "Страниц",
                        color = Color(78, 78, 78),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "Формат",
                        color = Color(78, 78, 78),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "Язык",
                        color = Color(78, 78, 78),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "ISBN", color = Color(78, 78, 78),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "Остаток на полках", color = Color(78, 78, 78),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                }
                Spacer(modifier = Modifier.width(62.dp))
                Column() {
                    Text(
                        text = book.bookInfo.author,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "2021 г.",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = book.shopOwner,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "Переплет",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "${book.bookInfo.numberOfPages} ст.",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "Печатная книга",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "Русский",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "9785716410091",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                    Text(
                        text = "38 шт.",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular,
                            )
                        )
                    )
                }
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
                        interactionSource =  MutableInteractionSource(),
                        indication = null,
                    ) {
                        onClickAddButton()

                        DataClickMetric(
                            Buttons.BUY_BOOK,
                            Screens.Recommendation.route
                        )

                    },
                contentAlignment = Alignment.Center
            ) {
                Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically){
                    Image(painter = painterResource(id = R.drawable.ic_plus), contentDescription =null)
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "В корзину ${book.bookInfo.price}₽",
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


    }
}

