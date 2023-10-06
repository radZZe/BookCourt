package com.example.bookcourt.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.ui.theme.MainBgColor

@Composable
fun WantToReadScreen(
    onNavigateToProfile: () -> Unit = {},
    viewModel: WantToReadViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getUser()
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBgColor)
    ) {
        ScreenHeader(text = "Хочу прочитать", goBack = { onNavigateToProfile() })
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentHeight()
//        ) {
//            items(viewModel.notifications) { notifications ->
//                OrderNotification(notification = notifications)
//            }
//        }
    }
}