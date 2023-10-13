package com.example.bookcourt.ui.library

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.ui.categorySelection.CategoryItem
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.WindowInfo
import com.example.bookcourt.utils.rememberWindowSizeClass
import com.skydoves.cloudy.Cloudy


@Composable
fun LibraryScreen(
    onNavigateToSearchScreen:()->Unit,
    onNavigateBookCard:(bookId:String)->Unit,
    viewModel: LibraryViewModel = hiltViewModel()) {
    val windowInfo = rememberWindowSizeClass()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit){
        viewModel.loadPopularBooks(context)
        viewModel.loadRecommendations(context)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MainBgColor)
            .padding(bottom = 16.dp)
    ) {
        SearchWidget(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            onNavigateToSearchScreen()
        }
        GenresBar(windowType = windowInfo.screenHeightInfo, viewModel = viewModel)
        SponsorsBar(viewModel = viewModel)
        RecommendationBar(viewModel = viewModel,onNavigateBookCard)
        PopularBar(viewModel = viewModel,onNavigateBookCard)
    }
}

@Composable
fun SearchWidget(modifier: Modifier,onClick:()->Unit){
    Box(
        modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(GrayBackground)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { onClick() }
    ){
        Text(
            text = stringResource(id = R.string.search_widget),
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = GrayText,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "SearchIcon",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp)
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
            Spacer(modifier = Modifier.size(16.dp))
            CategoryItem(
                text = category.value.title,
                windowType = windowType,
                isChecked = category.value.isSelected.value) {
               viewModel.filterCategories(category.value.title)
            }
            if (category.value==viewModel.categories.last().value){
                Spacer(modifier = Modifier.size(16.dp))
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
            Spacer(modifier = Modifier.size(16.dp))
            SponsorItem(
                sponsorImageId = partner.imageId,
                sponsorTitle = partner.title,
                sponsorText = partner.mainText
            )
            if (partner ==viewModel.partners.last()){
                Spacer(modifier = Modifier.size(16.dp))
            }
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
        .background(GrayBackground)
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
fun RecommendationBar(
    viewModel: LibraryViewModel,
    onNavigateBookCard:(bookId:String)->Unit,){
    Column (modifier = Modifier.fillMaxWidth()){
        Text(
            text = stringResource(id = R.string.library_screen_recommendations),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp)
        )
        if (viewModel.popularBooksFiltered.isEmpty()){
            CircularProgressIndicator(modifier = Modifier
                .scale(1f)
                .align(Alignment.CenterHorizontally)
            )
        }
        else{
            LazyRow(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(viewModel.recommendationBooksFiltered){ book->
                    Spacer(modifier = Modifier.size(16.dp))
                    BookItem(book = book!!,onNavigateBookCard)
                    if (book ==viewModel.recommendationBooksFiltered.last()){
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }

}

@Composable
fun PopularBar(
    viewModel: LibraryViewModel,
    onNavigateBookCard:(bookId:String)->Unit,){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.library_screen_popular),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.padding(16.dp)
        )
        if (viewModel.popularBooksFiltered.isEmpty()){
            CircularProgressIndicator(modifier = Modifier
                .scale(1f)
                .align(Alignment.CenterHorizontally)
            )
        }
        else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.popularBooksFiltered) { book ->
                    Spacer(modifier = Modifier.size(16.dp))
                    BookItem(book = book!!, onNavigateBookCard)
                    if (book == viewModel.popularBooksFiltered.last()) {
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }

}

@Composable
fun BookItem(
    book: Book,
    onNavigateBookCard:(bookId:String)->Unit,
) {
   Box(modifier = Modifier
       .height(120.dp)
       .width(83.dp)
       .shadow(
           elevation = 10.dp,
           shape = RoundedCornerShape(15.dp)
       )
   ) {
        SubcomposeAsyncImage(
            model = book.bookInfo.image,
            contentDescription = "${book.bookInfo.author}'s book image",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(15.dp))
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    onNavigateBookCard(book.isbn!!)
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
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.Center
        ){
           Image(
               painter = painterResource(id = R.drawable.ic_like),
               contentDescription = "LikeIcon",
               modifier = Modifier
                   .size(13.dp)
                   .align(Alignment.CenterVertically)
           )
           Spacer(modifier = Modifier.size(4.dp))
           Text(text = rating.toString(),
               fontFamily = Roboto,
               fontWeight = FontWeight.Bold,
               color = GreenText,
               fontSize = 14.sp,)
        }
    }
}

