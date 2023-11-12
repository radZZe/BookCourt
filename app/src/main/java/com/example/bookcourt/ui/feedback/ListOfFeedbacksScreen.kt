package com.example.bookcourt.ui.feedback

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.ui.recommendation.Feedback

@Composable
fun ListOfFeedbacksScreen(
    id: String,
    onNavigateToRecommendationScreen: () -> Unit,
    viewModel:ListOfFeedbackViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = Unit){
        viewModel.getFeedbacks(id)
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 12.dp, end = 12.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
        ) {
            Row(modifier = Modifier.align(Alignment.CenterStart)) {
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = null,
                    modifier = Modifier.clickable(interactionSource =  MutableInteractionSource(),
                        indication = null) {
                        onNavigateToRecommendationScreen()

                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    modifier = Modifier,
                    text = "Отзывы",
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontSize = 18.sp
                )
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(content =
        {
            items(viewModel.listUserFeedbacks) { item ->
                Feedback(
                    username = item.username,
                    rate = item.rate,
                    date = item.date,
                    description = item.description,
                    bgColor = Color(239, 235, 222)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

        }
        )
    }

}