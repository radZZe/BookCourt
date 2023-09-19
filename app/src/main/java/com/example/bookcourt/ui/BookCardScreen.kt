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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.book.BookInfo
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.recommendation.BookCardImage
import com.example.bookcourt.ui.recommendation.CategoriesBlock
import com.example.bookcourt.ui.recommendation.FeedbackBlock
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.utils.Buttons
import com.example.bookcourt.utils.Screens

@Composable
fun BookCardScreen(uri:String,book: Book){
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(
            MainBgColor
        )) {
        BookCardTopBar({},R.drawable.igra_slov_logo)
        Spacer(modifier = Modifier.height(18.dp))
        Box(Modifier.fillMaxWidth()){
            Box(modifier = Modifier
                .clip(RoundedCornerShape(23.dp))
                .fillMaxWidth(0.5f)
                .align(
                    Alignment.Center
                )){
                CardOfBook(uri)


            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        BookCardMainContent(book)

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
                modifier = Modifier.clickable {
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
fun BookCardMainContent(book:Book){
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
//            FeedbackBlock(
//                username = "username",
//                date = "date",
//                title = "title",
//                description = "Морские города имеют особое очарование. Родиться в одном из них – настоящая удача! И как же хочется, чтобы малыш скорее узнал о соленом ветре, высоких сопках и морских приключениях!",
//                rate = 5,
//                leaveFeedbackVisibility = false,
//                onNavigateToFeedback = {s->},
//                onClickRatingBar = {
//
//                },
//                disableLeaveFeedbackVisibility = {
//                }
//            )

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
                        text = "Диана Лютер",
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
                        text = "Лютература",
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
                        text = "152 ст.",
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
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = Color.Black),
                    ) {

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

//@Composable
//@Preview
//fun BookCardScreenPreview(){
//    var book:BookInfo = BookInfo(
//        title = "title",
//        author = "author",
//        description = "description",
//        numberOfPages = "numberOfPages",
//        rate = 3,
//        genre = "genre",
//        price = 286,
//        image = "https://i0.wp.com/igraslov.store/wp-content/uploads/2023/02/24c468ca-1139-4bc7-b5b5-9757543c62a2_d294c85e-f1e9-43f9-b69e-134e03136f17.jpeg?fit=361%2C575&ssl=1"
//    )
//    val uri = "https://i0.wp.com/igraslov.store/wp-content/uploads/2023/02/24c468ca-1139-4bc7-b5b5-9757543c62a2_d294c85e-f1e9-43f9-b69e-134e03136f17.jpeg?fit=361%2C575&ssl=1"
//    BookCardScreen(uri,book)
//}
