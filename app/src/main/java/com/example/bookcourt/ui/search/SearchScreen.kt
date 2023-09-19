package com.example.bookcourt.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.user.SearchRequest
import com.example.bookcourt.ui.theme.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val searchText by viewModel.searchText.collectAsState()
    val books by viewModel.books.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val isDisplayed by viewModel.isDisplayed.collectAsState()
    val recentRequests by viewModel.recentRequests.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllBooks(context)
        viewModel.getSearchRequests()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MenuBackGround)
    ) {
        TextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChange,
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                textColor = PrimaryText,
                placeholderColor = SecondaryText,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                backgroundColor = SearchFieldBackground,
                cursorColor = PrimaryText
            ),
            placeholder = { Text("Найти книгу или автора") },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close_circle),
                    contentDescription = "Trailing Icon",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { viewModel.onClearSearchText() }
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Leading Icon",
                    modifier = Modifier
                        .size(42.dp)
                        .clickable {
                            onNavigateBack()
                        }

                )
            },
            textStyle = TextStyle(fontFamily = Roboto),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    viewModel.addSearchRequest(searchText)
                }
            )
        )
        if (isDisplayed) {
            if (isSearching) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                if (books.isEmpty()) {
                    Text(
                        text = "К сожалению, ничего не найдено, возможно Вас заинтересуют следующие книги:",
                        fontSize = 16.sp,
                        fontFamily = Roboto,
                        color = SecondaryText,
                        modifier = Modifier.padding(top = 20.dp, start = 20.dp)
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .weight(1f)
                ) {
                    items(books.ifEmpty { viewModel.recommendedBooks }) { book ->
                        SearchBookCard(book = book)
                    }
                }
            }
        } else {
            RecentRequests(recentRequests, viewModel)
        }
    }
}

@Composable
fun SearchBookCard(book: Book) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(top = 20.dp)
    ) {
        SubcomposeAsyncImage(
            model = book.bookInfo.image,
            contentDescription = "${book.bookInfo.title} image",
            modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.FillBounds,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .scale(0.5f)
                )
            }
        )
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(start = 20.dp)
        ) {
            Text(
                text = book.bookInfo.title,
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                color = PrimaryText,
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = book.bookInfo.author,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = SecondaryText,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun RecentRequests(searchRequests: List<SearchRequest>, viewModel: SearchViewModel) {
    if (searchRequests.isNotEmpty()) {
        Text(
            text = "Ваши последние запросы:",
            fontSize = 16.sp,
            fontFamily = Roboto,
            color = PrimaryText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        items(searchRequests) { searchRequest ->
            Text(
                text = searchRequest.request,
                fontSize = 16.sp,
                fontFamily = Roboto,
                color = SecondaryText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .clickable {
                        viewModel.onSearchTextChange(searchRequest.request)
                    }

            )
        }
    }

}
