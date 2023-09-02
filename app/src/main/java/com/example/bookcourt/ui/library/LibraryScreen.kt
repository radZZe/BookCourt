package com.example.bookcourt.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.ui.categorySelection.BoxItem
import com.example.bookcourt.ui.categorySelection.CategorySelectionScreen
import com.example.bookcourt.ui.theme.Roboto
import com.example.bookcourt.utils.Constants.genres
import com.example.bookcourt.utils.WindowInfo
import com.example.bookcourt.utils.rememberWindowSizeClass

@Composable
fun LibraryScreen(onNavigateToRecommendation:()->Unit) {
    val windowInfo = rememberWindowSizeClass()
    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SearchWidget {

        }
        GenresBar(windowType = windowInfo.screenHeightInfo)
    }
}

@Composable
fun SearchWidget(onClick:()->Unit){
    Box(modifier = Modifier
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
        Icon(
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
    viewModel: LibraryViewModel = hiltViewModel(),
    windowType:WindowInfo.WindowType
){
    LazyRow(modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(viewModel.categories){ category ->
            BoxItem(
                text = category.value.title,
                windowType = windowType,
                isChecked = category.value.state.value) {
                //todo
            }
        }
    }
}

@Composable
fun SponsorsBar(){
    Box(modifier = Modifier
        .clip(shape = RoundedCornerShape(12.dp))
    ){

    }
}

