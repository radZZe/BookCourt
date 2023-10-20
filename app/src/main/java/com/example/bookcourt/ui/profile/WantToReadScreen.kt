package com.example.bookcourt.ui.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.ui.basket.basketScreen.BasketBookCard
import com.example.bookcourt.ui.theme.DisabledBtnText
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.ui.theme.dimens

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
        ScreenHeader(
            text = "Хочу прочитать",
            goBack = {
                onNavigateToProfile()
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            viewModel.basketItems.forEachIndexed { index, item ->
                WantToReadItem(
                    item = item,
                    onPlusClick = { viewModel.increaseTheAmount(index) },
                    onMinusClick = { viewModel.reduceTheAmount(index) },
                    onDeleteItem = { viewModel.deleteBasketItem(item) },
                    onChangeState = { viewModel.changeItemSelectState(index) }
                )
            }
        }
    }
}

@Composable
fun WantToReadItem(
    item: BasketItem,
    onPlusClick: () -> Unit,
    onMinusClick: () -> Unit,
    onDeleteItem: () -> Unit,
    onChangeState: () -> Unit,
) {
    val book = item.data
    Row(
        Modifier
            .height(MaterialTheme.dimens.basketBookCardHeight.dp)
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.25f)
                .clip(RoundedCornerShape(15.dp))
        ) {
            BasketBookCard(book.bookInfo.image)
        }
        Spacer(modifier = Modifier.width(15.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 3.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = book.bookInfo.title,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.paddingSmall.dp))
            Text(
                text = book.bookInfo.genre,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 14.sp,
                color = DisabledBtnText
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.paddingBig.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${book.bookInfo.price}₽",
                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                    fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "800₽",
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontSize = 14.sp,
                    color = DisabledBtnText
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.paddingSmall.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.3f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(id = R.drawable.like_book_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onChangeState() },
                    tint = if (item.isSelected) Color.Red else Color.Gray
                    )
                    Image(
                        painterResource(id = R.drawable.ic_delete),
                        contentDescription = null,
                        modifier = Modifier.clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            onDeleteItem()
                        }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(0.80f),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(MaterialTheme.dimens.amountButtonsHeight.dp)
                            .clip(
                                RoundedCornerShape(5.dp)
                            )
                            .background(Color(239, 235, 222))
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                if (item.amount > 0) {
                                    onMinusClick()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_minus),
                            contentDescription = null,
                            modifier = Modifier.size(MaterialTheme.dimens.amountButtonsSize.dp)
                        )
                    }
                    Text(text = item.amount.toString())
                    Box(
                        modifier = Modifier
                            .height(MaterialTheme.dimens.amountButtonsHeight.dp)
                            .clip(
                                RoundedCornerShape(5.dp)
                            )
                            .background(Color(239, 235, 222))
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) { onPlusClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_plus),
                            contentDescription = null,
                            modifier = Modifier.size(MaterialTheme.dimens.amountButtonsSize.dp)
                        )
                    }
                }
            }
        }
    }
}