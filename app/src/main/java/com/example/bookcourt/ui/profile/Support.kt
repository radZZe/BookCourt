package com.example.bookcourt.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.MainBgColor


@Composable
fun Support(
    onNavigateToProfile: () -> Unit,
    onNavigateToAskQuestion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBgColor)
    ) {
        ScreenHeader(
            text = "О приложении",
            goBack = { onNavigateToProfile() }
        )
        Spacer(modifier = Modifier.height(20.dp))
        SupportOptions { onNavigateToAskQuestion() }
    }
}

@Composable
private fun SupportOptions(
    onNavigateToAskQuestion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(DarkBgColor)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        OptionItem(
            icon = R.drawable.ic_messages,
            text = "Задать вопрос",
            onClick = { onNavigateToAskQuestion() })
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 10.dp)
                .background(Color.LightGray)
        )
        OptionItem(R.drawable.ic_question, "Ответы на частые вопросы")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 10.dp)
                .background(Color.LightGray)
        )
        OptionItem(
            icon = R.drawable.ic_telegram,
            text = "Телеграм",
            onClick = { }
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}