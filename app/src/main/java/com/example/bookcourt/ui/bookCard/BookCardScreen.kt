package com.example.bookcourt.ui


import androidx.compose.foundation.*

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.bookCard.BookCardViewModel
import com.example.bookcourt.ui.error_pages.ErrorPage
import com.example.bookcourt.ui.error_pages.ErrorType
import com.example.bookcourt.ui.recommendation.BookCardImage
import com.example.bookcourt.ui.recommendation.CategoriesBlock
import com.example.bookcourt.ui.recommendation.FeedbackBlock
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.ui.theme.dimens
import com.example.bookcourt.utils.Buttons
import com.example.bookcourt.utils.Screens
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun BookCardScreen(
    bookId: String,
    onNavigateBack: () -> Unit,
    onNavigateLeaveFeedback: (title: String, rate: Int) -> Unit,
    onNavigateListFeedbacks: (title: String) -> Unit,
    viewModel: BookCardViewModel = hiltViewModel(),
    feedbackText: String? = null,
    needToUpdate: Boolean = false,
    newRate: Int? = null
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        viewModel.getBookByID(context, bookId)
        //viewModel.testGetBookById("92709a69-9f07-41a3-92ab-a78ee91bfe12")
    }
    val book = viewModel.book.value
    if (needToUpdate && newRate != null) {
        book!!.bookInfo.rate = newRate.toFloat()
    }
    if(viewModel.errorPage.value){
        ErrorPage(errorType = ErrorType.UnknownError){
            viewModel.testGetBookById("92709a69-9f07-41a3-92ab-a78ee91bfe12")
        }
    }else{
        if (book != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(
                        MainBgColor
                    )
            )
            {
                BookCardTopBar(onBackNavigate = { onNavigateBack() },
                    imageLogo = R.drawable.igra_slov_logo,
                    addToFavorite = { viewModel.addToFavorite(book) },
                    isFavorite = viewModel.isFavorite.value,
                    isAuthenticated = viewModel.isAuthenticated.collectAsState(initial = false).value
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.paddingBig.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.dimens.bookCardHeight.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(23.dp))
                            .fillMaxWidth(0.5f)
                            .align(
                                Alignment.Center
                            )
                    ) {
                        CardOfBook(book.bookInfo.image)


                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                BookCardMainContent(
                    feedbackText, needToUpdate, book, onNavigateLeaveFeedback, onNavigateListFeedbacks,
                    {
                        viewModel.addBasketItem(
                            BasketItem(
                                data = book
                            )
                        )
                    },
                    viewModel.isActiveBasket.value,
                    viewModel.isAuthenticated.collectAsState(initial = false).value
                )

            }
        }
    }



}

@Composable
fun BookCardTopBar(
    isAuthenticated: Boolean,
    isFavorite: Boolean,
    onBackNavigate: () -> Unit,
    imageLogo: Int,
    addToFavorite: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
        //.height(55.dp)
    ) {
        Row(modifier = Modifier.align(Alignment.CenterStart)) {
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        onBackNavigate()

                    }
                    .size(MaterialTheme.dimens.iconSizeSmall.dp)
            )
        }
        Image(
            painter = painterResource(id = imageLogo),
            contentDescription = null,
            modifier = Modifier
                .align(
                    Alignment.Center
                )
                .size(MaterialTheme.dimens.iconSizeBig.dp)
        )
        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            if(isAuthenticated){
                Image(
//                    id = if (isFavorite) R.drawable.ic_favorite_fill else R.drawable.favorite_book_topbar)
                    painter = painterResource(id = R.drawable.favorite_book_topbar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(MaterialTheme.dimens.iconSizeBig.dp)
                        .clickable {
                            addToFavorite()
                        },
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isFavorite) ColorFilter.tint(Color.Yellow) else null

                )
            }

            Image(
                painter = painterResource(id = R.drawable.share_icon),
                contentDescription = null,
                modifier = Modifier.size(MaterialTheme.dimens.iconSizeBig.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun CardOfBook(uri: String) {
    BookCardImage(uri = uri, true)

}

@Composable
fun BookCardMainContent(
    feedbackText: String?,
    needToUpdate: Boolean,
    book: Book,
    onNavigateLeaveFeedback: (title: String, rate: Int) -> Unit,
    onNavigateListFeedbacks: (title: String) -> Unit,
    onClickAddButton: () -> Unit,
    isActiveBasket: Boolean,
    isAuthenticated: Boolean,
) {
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
                    text = "${book.bookInfo.author} | ${book.bookInfo.genre}",
                    color = Color(134, 134, 134),
                    style = MaterialTheme.typography.subtitle1,
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
                        style = MaterialTheme.typography.subtitle2,
                    )
                    Text(
                        text = "Лайки",
                        color = Color(134, 134, 134),
                        style = MaterialTheme.typography.subtitle2,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "124",
                        color = Color.Black,
                        style = MaterialTheme.typography.subtitle2,
                    )
                    Text(
                        text = "Дизлайки",
                        color = Color(134, 134, 134),
                        style = MaterialTheme.typography.subtitle2,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "56",
                        color = Color.Black,
                        style = MaterialTheme.typography.subtitle2,
                    )
                    Text(
                        text = "Интересуются",
                        color = Color(134, 134, 134),
                        style = MaterialTheme.typography.subtitle2,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = book.bookInfo.rate.toString(),
                        color = Color.Black,
                        style = MaterialTheme.typography.subtitle2,
                    )
                    Text(
                        text = "Оценка",
                        color = Color(134, 134, 134),
                        style = MaterialTheme.typography.subtitle2,
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
                onNavigateToFeedback = { s -> onNavigateListFeedbacks(s) },
                onClickRatingBar = {
                    onNavigateLeaveFeedback(book.bookInfo.title, it)
                },
                disableLeaveFeedbackVisibility = {

                },
                frontItem = book,
                isNeedToUpdateFeedback = needToUpdate,
                isAuthenticated = isAuthenticated

            )

            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
            )
            val list = listOf("Детективы", "Детская литература", "Рассказы")
            CategoriesBlock(list)
            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        //.height(134.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "Описание",
                        color = Color.Black,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = book.bookInfo.description,
                        style = MaterialTheme.typography.body1,
                        color = Color.Black,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .height(MaterialTheme.dimens.paddingBig.dp)
                    .fillMaxWidth()
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Column() {
                    Text(
                        text = "Автор",
                        color = Color(78, 78, 78),
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "Год издания",
                        color = Color(78, 78, 78),
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "Издательство",
                        color = Color(78, 78, 78),
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "Тип обложки",
                        color = Color(78, 78, 78),
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "Страниц",
                        color = Color(78, 78, 78),
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "Формат",
                        color = Color(78, 78, 78),
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "Язык",
                        color = Color(78, 78, 78),
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "ISBN", color = Color(78, 78, 78),
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "Остаток на полках", color = Color(78, 78, 78),
                        style = MaterialTheme.typography.body1,
                    )
                }
                Spacer(modifier = Modifier.width(62.dp))
                Column() {
                    Text(
                        text = book.bookInfo.author,
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "2021 г.",
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = book.shopOwner,
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "Переплет",
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "${book.bookInfo.numberOfPages} ст.",
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "Печатная книга",
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "Русский",
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "9785716410091",
                        style = MaterialTheme.typography.body1,
                    )
                    Text(
                        text = "38 шт.",
                        style = MaterialTheme.typography.body1,
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
                    .height(MaterialTheme.dimens.bookCardButtonSize.dp)
                    .clip(RoundedCornerShape(65))
                    .background(
                        if (isActiveBasket) Color(252, 225, 129) else Color(
                            179,
                            179,
                            179,
                            255
                        )
                    )
                    .padding(
                        top = MaterialTheme.dimens.paddingSmall.dp,
                        bottom = MaterialTheme.dimens.paddingSmall.dp
                    )
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                    ) {
                        if (isActiveBasket) {
                            onClickAddButton()

                            DataClickMetric(
                                Buttons.BUY_BOOK,
                                Screens.Recommendation.route
                            )
                        }


                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isActiveBasket) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_plus),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "В корзину ${book.bookInfo.price}₽",
                            color = Color.Black,
                            style = MaterialTheme.typography.body1,
                        )
                    } else {
                        Text(
                            text = "Уже в корзине",
                            color = Color.Black,
                            style = MaterialTheme.typography.body1,
                        )
                    }


                }

            }
        }


    }
}

