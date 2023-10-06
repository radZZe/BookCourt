package com.example.bookcourt.ui.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.utils.TextRobotoRegular


@Composable
fun OrdersScreen(
    onNavigateToProfile: () -> Unit = {},
    viewModel: OrdersViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBgColor)
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(text = "Заказы", goBack = { onNavigateToProfile() })
        OrdersOptions(viewModel = viewModel)
    }

}

@Composable
fun OrdersOptions(viewModel: OrdersViewModel) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
                .clip(
                    AbsoluteRoundedCornerShape(
                        topLeft = 15.dp,
                        bottomLeft = 15.dp,
                        topRight = 0.dp,
                        bottomRight = 0.dp
                    )
                )
                .border(
                    1.dp, BorderColor, AbsoluteRoundedCornerShape(
                        topLeft = 15.dp,
                        bottomLeft = 15.dp,
                        topRight = 0.dp,
                        bottomRight = 0.dp
                    )
                )
                .background(if (viewModel.option != OrderOptions.ACTIVE) MainBgColor else DarkBgColor)
                .clickable(interactionSource =  MutableInteractionSource(),
                    indication = null) {
                    viewModel.onOptionChanged(OrderOptions.ACTIVE)
                },
            contentAlignment = Alignment.Center
        ) {
            TextRobotoRegular(
                text = "Активные",
                color = Color.Black,
                fontSize = 14,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(
                    AbsoluteRoundedCornerShape(
                        topLeft = 0.dp,
                        bottomLeft = 0.dp,
                        topRight = 15.dp,
                        bottomRight = 15.dp
                    )
                )
                .border(
                    1.dp, BorderColor, AbsoluteRoundedCornerShape(
                        topLeft = 0.dp,
                        bottomLeft = 0.dp,
                        topRight = 15.dp,
                        bottomRight = 15.dp
                    )
                )
                .background(if (viewModel.option != OrderOptions.BOUGHT) MainBgColor else DarkBgColor)
                .clickable(interactionSource =  MutableInteractionSource(),
                    indication = null) {
                    viewModel.onOptionChanged(OrderOptions.BOUGHT)
                },
            contentAlignment = Alignment.Center
        ) {
            TextRobotoRegular(
                text = "Выкупленные",
                color = Color.Black,
                fontSize = 14,
            )
        }
    }
}