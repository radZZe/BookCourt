package com.example.bookcourt.ui.library

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.ui.categorySelection.BoxItem
import com.example.bookcourt.ui.theme.Roboto
import com.example.bookcourt.utils.WindowInfo
import com.example.bookcourt.utils.rememberWindowSizeClass




@Composable
fun LibraryScreen(onNavigateToSearchScreen:()->Unit, viewModel: LibraryViewModel = hiltViewModel()) {
    val windowInfo = rememberWindowSizeClass()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        viewModel.loadPopularBooks(context)
        viewModel.loadRecommendations(context)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        SearchWidget(modifier = Modifier.padding(8.dp)) {
            onNavigateToSearchScreen()
        }
        GenresBar(windowType = windowInfo.screenHeightInfo, viewModel = viewModel)
        SponsorsBar(viewModel = viewModel)
        RecommendationBar(viewModel = viewModel)
        PopularBar(viewModel = viewModel)
    }
}

@Composable
fun SearchWidget(modifier: Modifier,onClick:()->Unit){
    Box(
        modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(Color.LightGray)
            .clickable { onClick() }
    ){
        Text(
            text = "Search",
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "SearchIcon",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(8.dp)
        )
    }
}

@Composable
fun GenresBar(
    viewModel: LibraryViewModel,
    windowType:WindowInfo.WindowType
){
    LazyRow(modifier = Modifier
        .fillMaxWidth()
    ){
        items(viewModel.categories){ category ->
            Spacer(modifier = Modifier.size(8.dp))
            BoxItem(
                text = category.value.title,
                windowType = windowType,
                isChecked = category.value.state.value) {
               viewModel.filterCategories(category.value.title)
            }
        }
    }
}

@Composable
fun SponsorsBar(viewModel: LibraryViewModel){
    LazyRow(modifier = Modifier
        .fillMaxWidth()
    ){
        items(viewModel.partners){ partner->
            Spacer(modifier = Modifier.size(8.dp))
            SponsorItem(
                sponsorImageId = partner.imageId,
                sponsorTitle = partner.title,
                sponsorText = partner.mainText
            )
        }
    }
}

@Composable
fun SponsorItem(
    sponsorImageId:Int,
    sponsorTitle:String,
    sponsorText:String
){
    Column(modifier = Modifier
        .clip(shape = RoundedCornerShape(12.dp))
        .background(Color.LightGray)
        .width(380.dp)
    ){
        Image(
            painter = painterResource(id = sponsorImageId),
            contentDescription = "Sponsor's image",
            modifier = Modifier
                .width(323.dp)
                .height(200.dp)
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = sponsorTitle,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.padding( horizontal = 8.dp)
        )
        Text(
            text = sponsorText,
            fontFamily = Roboto,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )

    }
}

@Composable
fun RecommendationBar(viewModel: LibraryViewModel){
    Column {
        Text(
            text = "Вам может понравиться",
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )
        LazyRow(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(viewModel.recommendationBooksFiltered){ book->
                Spacer(modifier = Modifier.size(8.dp))
                BookItem(book = book!!)
            }
        }
    }

}

@Composable
fun PopularBar(viewModel: LibraryViewModel){
    Column {
        Text(
            text = "Популярно сегодня",
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )
        LazyRow(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(viewModel.popularBooksFiltered){ book->
                Spacer(modifier = Modifier.size(8.dp))
                BookItem(book = book!!)
            }
        }
    }

}

@Composable
fun BookItem(
    book: Book
) {
   Box(modifier = Modifier
       .height(120.dp)
       .width(83.dp)
   ) {
        SubcomposeAsyncImage(
            model = book.bookInfo.image,
            contentDescription = "${book.bookInfo.author}'s book image",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(15.dp))
                .clickable {
                   //Todo("переадресация на карточку книги")
                },
            contentScale = ContentScale.FillBounds,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .scale(0.5f)
                )
            }
        )
       RatingIcon(
           rating = book.bookInfo.rate,
           modifier = Modifier
               .align(Alignment.BottomStart)
               .padding(8.dp)
               .height(24.dp)
               .width(49.dp)
       )
    }
}

@Composable
fun RatingIcon(rating:Float, modifier: Modifier){
    Box(
        modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White),
    ){
        Row (
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 1.dp),
            horizontalArrangement = Arrangement.Center
        ){
           Image(
               painter = painterResource(id = R.drawable.ic_like),
               contentDescription = "LikeIcon",
               modifier = Modifier.size(17.dp)
           )
           Spacer(modifier = Modifier.size(4.dp))
           Text(text = rating.toString())
        }
    }
}

