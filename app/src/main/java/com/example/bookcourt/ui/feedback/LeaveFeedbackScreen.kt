package com.example.bookcourt.ui.feedback

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.ui.recommendation.RatingBar
import com.example.bookcourt.ui.recommendation.RecommendationTopBar

@Composable
fun LeaveFeedbackScreen(
    title: String,
    rate: Int,
    onNavigateToRecommendationScreen: (description:String,needToUpdate:Boolean) -> Unit,
    viewModel:LeaveFeedbackViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        RecommendationTopBar(
            visibility = true,
            onClickBackArrow = { onNavigateToRecommendationScreen(viewModel.feedbackText.value,false) },
            title = title
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_confirm),
                contentDescription = null,
                modifier = Modifier.clickable {
                    viewModel.feedbackIsConfirm.value = true
                    onNavigateToRecommendationScreen(viewModel.feedbackText.value,true)

                },
                contentScale = ContentScale.Crop

            )
        }
        Column() {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(16.dp, bottom = 10.dp)
            ) {
                RatingBar(
                    rate = rate,
                    enabled = false,
                    numberOfSelectedStarsDefault = rate,
                    starSize = 28,
                    onClick = {},
                    disableLeaveFeedbackVisibility = {})
            }
            TextField(
                modifier = Modifier.fillMaxSize(),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
                value = viewModel.feedbackText.value,
                onValueChange = {
                    viewModel.feedbackText.value = it
                },
                placeholder = {
                    Text(
                        text = "Что вы думаете об этой книге?",
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 16.sp,
                        color = Color(134, 134, 134)
                    )
                },
            )
        }

    }


}