package com.example.bookcourt.ui.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.ui.basket.successPurchase.OrderOptions
import com.example.bookcourt.ui.basket.successPurchase.OrderWidget
import com.example.bookcourt.ui.basket.successPurchase.SuccessPurchaseViewModel
import com.example.bookcourt.ui.theme.Brown
import com.example.bookcourt.ui.theme.GrayBackground
import com.example.bookcourt.ui.theme.LightYellowBtn
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.utils.CustomButton
import com.example.bookcourt.utils.TextRobotoBold
import com.example.bookcourt.utils.TextRobotoRegular
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrdersScreen(
    onNavigateToProfile: () -> Unit = {},
    viewModel: SuccessPurchaseViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetContent = {
            BottomSheetContent(
                viewModel = viewModel,
                onButtonClick = {
                    viewModel.sendDeclineOrderReason()
                    scope.launch {
                        sheetState.hide()
                    }
                }
            )
        },
        sheetBackgroundColor = MainBgColor,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        scrimColor = ModalBottomSheetDefaults.scrimColor,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MainBgColor)
        ) {
            ScreenHeader(text = "Заказы", goBack = { onNavigateToProfile() })
            OrdersOptions(viewModel = viewModel)
            if (viewModel.option == OrderOptions.ACTIVE) {
                OrdersList(
                    viewModel = viewModel,
                    buttonText = "Отменить заказ",
                    onButtonClick = {
                        scope.launch {
                            sheetState.show()
                        }
                    }
                )
            } else {
                OrdersList(
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun OrdersOptions(
    viewModel: SuccessPurchaseViewModel
) {
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
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
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
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
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

@Composable
fun OrdersList(
    buttonText: String = "Оставить отзыв",
    onButtonClick: () -> Unit = {},
    viewModel: SuccessPurchaseViewModel
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getOrder()
    }

    if (viewModel.order.value != null) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(MainBgColor)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OrderWidget(viewModel.order.value!!)
                CustomButton(
                    text = buttonText,
                    modifier = Modifier.fillMaxWidth(),
                    color = GrayBackground,
                    textColor = Color.Black,
                    onCLick = {
                        onButtonClick()
                    }
                )
            }
        }
    }
}

@Composable
fun BottomSheetContent(
    viewModel: SuccessPurchaseViewModel,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .verticalScroll(rememberScrollState())
            .background(MainBgColor)
            .padding(20.dp)
    ) {
        TextRobotoBold(
            text = "Пожалуйста, укажите причину отмены заказа",
            color = Color.Black,
            fontSize = 14
        )
        Spacer(modifier = Modifier.height(6.dp))
        TextRobotoRegular(
            text = "Это поможет сделать нам сервис лучше",
            color = Color.Gray,
            fontSize = 12
        )
        Spacer(modifier = Modifier.height(12.dp))
        BottomSheetCheckboxItem(
            text = "Уже купил книгу по более низкой цене",
            checkboxState = viewModel.checkbox1State,
            onValueChange = { viewModel.onCheckbox1StateChange() }
        )
        BottomSheetCheckboxItem(
            text = "Перехотел читать эту книгу",
            checkboxState = viewModel.checkbox2State,
            onValueChange = { viewModel.onCheckbox2StateChange() }
        )
        BottomSheetCheckboxItem(
            text = "Другое",
            checkboxState = viewModel.checkbox3State,
            onValueChange = { viewModel.onCheckbox3StateChange() }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            placeholder = {
                TextRobotoRegular(
                    text = "Причина отмены",
                    color = Color.Gray,
                    fontSize = 14
                )
            },
            value = viewModel.reason,
            onValueChange = { viewModel.onReasonChanged(it) },
            maxLines = 6,
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = DarkBgColor,
                cursorColor = Color.Black,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        )
        CustomButton(
            modifier = Modifier.padding(top = 16.dp),
            textColor = Color.Black,
            text = "Отменить заказ",
            onCLick = { onButtonClick() }
        )
    }
}

@Composable
fun BottomSheetCheckboxItem(
    onValueChange: () -> Unit,
    checkboxState: Boolean,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Checkbox(
            checked = checkboxState,
            onCheckedChange = { onValueChange() },
            modifier = Modifier.size(24.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = LightYellowBtn,
                uncheckedColor = DarkGreyColor,
                checkmarkColor = Color.Black,
            )
        )
        Spacer(modifier = Modifier.width(10.dp))
        TextRobotoRegular(text = text, color = Color.Black, fontSize = 16)
    }
}