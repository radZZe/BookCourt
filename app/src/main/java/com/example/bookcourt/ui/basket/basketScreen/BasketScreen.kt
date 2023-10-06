package com.example.bookcourt.ui.basket.basketScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.bookcourt.R
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.basket.emptyBasketScreen.EmptyBasketScreen
import com.example.bookcourt.ui.theme.ActiveProgressGrey
import com.example.bookcourt.ui.theme.DisabledBtnText
import com.example.bookcourt.utils.Buttons
import com.example.bookcourt.utils.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BasketScreen(
    viewModel: BasketViewModel = hiltViewModel(),
    onNavigateToOrdering:()->Unit,
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit,) {
        launch(Dispatchers.IO) {
            viewModel.getItems()
        }
    }

    Box(Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            BasketTopBar(numberItems = viewModel.basketItems.size,
                stateSelectAll = viewModel.stateSelectAll.value,
                onStateSelectAllChanged = {
                    viewModel.selectAll()
                },
                onDeleteSelected = { viewModel.deleteSelected() })
            if (viewModel.basketItems.isEmpty()) {
                EmptyBasketScreen()
            } else {
                for (i in 0..viewModel.owners.size - 1) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasketCheckBox(modifier = Modifier, viewModel.owners[i].isSelected) {
                            viewModel.changeItemSelectStateByOwner(viewModel.owners[i].value, i)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = viewModel.owners[i].value,
                            fontFamily = FontFamily(Font(R.font.roboto_bold)),
                            fontSize = 18.sp
                        )
                    }
                    viewModel.basketItems.forEachIndexed { index, basketItem ->
                        if (basketItem.data.shopOwner == viewModel.owners[i].value) {
                            OrderItem(item = basketItem,
                                onPlusClick = { viewModel.increaseTheAmount(index) },
                                onMinusClick = { viewModel.reduceTheAmount(index) },
                                onStateSelectedChange = { viewModel.changeItemSelectState(index) },
                                onDeleteItem = { viewModel.deleteBasketItem(basketItem) })
                        }
                    }
                }
            }

        }
        if (viewModel.isBasketItemsHasSelected()) {
            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()) {

                Divider(color = Color(239, 235, 222), thickness = 1.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp, 8.dp)
                    , horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {
                        Text(
                            text = "${viewModel.getSelectedItems()} товара", fontFamily = FontFamily(
                                Font(R.font.roboto_regular)
                            ), fontSize = 14.sp, color = Color(78, 78, 78)
                        )
                        Text(
                            text = "${viewModel.getPrice()}₽",
                            fontFamily = FontFamily(
                                Font(R.font.roboto_regular)
                            ),
                            fontSize = 18.sp,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(65))
                            .background(Color(252, 225, 129))
                            .padding(top = 12.dp, bottom = 12.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(color = Color.Black),
                            ) {
                                //onClickAddButton()
                                onNavigateToOrdering()
                                DataClickMetric(
                                    Buttons.BUY_BOOK, Screens.Recommendation.route
                                )

                            }, contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "К оформлению",
                                color = Color.Black,
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.roboto_regular,
                                    )
                                ),
                                fontSize = 16.sp,
                            )

                        }

                    }


                }
                Divider(color = Color(239, 235, 222), thickness = 1.dp)
            }

        }
    }


}

@Composable
fun BasketTopBar(
    numberItems: Int,
    onStateSelectAllChanged: () -> Unit,
    stateSelectAll: Boolean,
    onDeleteSelected: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(40.dp, 60.dp)
                .padding(12.dp, 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Корзина",
                    fontFamily = FontFamily(
                        Font(R.font.roboto_bold)
                    ),
                    fontSize = 20.sp,
                )
                if (numberItems > 0) {
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "${numberItems} товара", fontFamily = FontFamily(
                            Font(R.font.roboto_regular)
                        ), fontSize = 14.sp, color = Color(78, 78, 78)
                    )
                }
            }


        }
        if (numberItems > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.35f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    var isChecked = remember { mutableStateOf(false) }
                    BasketCheckBox(Modifier, stateSelectAll) {
                        onStateSelectAllChanged()
                    }
                    Text(
                        text = "Выбрать все",
                        fontFamily = FontFamily(
                            Font(R.font.roboto_regular)
                        ),
                        fontSize = 14.sp,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .clickable {
                            onDeleteSelected()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(painterResource(id = R.drawable.ic_delete), contentDescription = null)
                    Text(
                        text = "Удалить выбранное",
                        fontFamily = FontFamily(
                            Font(R.font.roboto_regular)
                        ),
                        fontSize = 14.sp,
                    )
                }

            }

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(239, 235, 222))
        )

    }

}

//@Preview
//@Composable
//fun PreviewBasketScreen(){
//    BasketScreen()
//}

@Composable
fun OrderItem(
    item: BasketItem,
    onPlusClick: () -> Unit,
    onMinusClick: () -> Unit,
    onStateSelectedChange: () -> Unit,
    onDeleteItem: () -> Unit,

    ) {

    val book = item.data
    Row(
        Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(start = 0.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
            .background(White),
    ) {
        Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
            //var isChecked = remember { mutableStateOf(false) }
            BasketCheckBox(modifier = Modifier, item.isSelected) {
                onStateSelectedChange()
            }
        }

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
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = book.bookInfo.genre,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                fontSize = 14.sp,
                color = DisabledBtnText
            )
            Spacer(modifier = Modifier.height(18.dp))
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
            Spacer(modifier = Modifier.height(10.dp))
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
                    Image(
                        painterResource(id = R.drawable.ic_favorite),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Image(painterResource(id = R.drawable.ic_delete),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onDeleteItem()
                        })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(0.80f),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(55.dp)
                            .clip(
                                RoundedCornerShape(5.dp)
                            )
                            .background(Color(239, 235, 222))
                            .clickable {
                                if (item.amount > 0) {
                                    onMinusClick()
                                }

                            }, contentAlignment = Alignment.Center

                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_minus),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Text(text = item.amount.toString())
                    Box(
                        modifier = Modifier
                            .height(35.dp)
                            .clip(
                                RoundedCornerShape(5.dp)
                            )
                            .background(Color(239, 235, 222))
                            .clickable {
                                onPlusClick()
                            }, contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painterResource(id = R.drawable.ic_plus),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BasketCheckBox(
    modifier: Modifier = Modifier, checked: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    Checkbox(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = CheckboxDefaults.colors(
            checkedColor = Color(252, 225, 129),
            uncheckedColor = ActiveProgressGrey,
            checkmarkColor = Color.Black
        )
    )
}


@Composable
fun BasketBookCard(uri: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(uri).size(Size.ORIGINAL).build(),
    )

    if (painter.state is AsyncImagePainter.State.Loading) {
        CircularProgressIndicator()
    } else {
        Image(
            painter = painter,
            contentDescription = stringResource(R.string.book_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
        )
    }
}
