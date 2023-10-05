package com.example.bookcourt.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.utils.TextRobotoBold
import com.example.bookcourt.utils.TextRobotoRegular


@Composable
fun OrdersNotificationsScreen(
    onNavigateToProfile: () -> Unit,
    viewModel: OrdersNotificationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBgColor)
    ) {
        ScreenHeader(text = "Уведомления", goBack = { onNavigateToProfile() })
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            items(viewModel.notifications) { notifications ->
                OrderNotification(notification = notifications)
            }
        }
    }
}

@Composable
private fun OrderNotification(
    notification: OrderNotification
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
    {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(10.dp))
                .background(DarkBgColor)
        ) {
            Icon(
                painter = painterResource(id = notification.icon),
                contentDescription = "Item Icon",
                modifier = Modifier.padding(6.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            TextRobotoBold(text = notification.header, color = Color.Black, fontSize = 16)
            Spacer(modifier = Modifier.height(2.dp))
            TextRobotoRegular(text = notification.text, color = Color.Black, fontSize = 12)
        }
    }
}