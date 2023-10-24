package com.example.bookcourt.ui.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.Buttons
import com.example.bookcourt.utils.Screens
import kotlinx.coroutines.delay

@Composable
fun VerificationCodeScreen(
    onNavigateToTutorial: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    viewModel: VerificationCodeViewModel = hiltViewModel()
) {
    val timer by viewModel.timer.collectAsState()
    val isOver by viewModel.isOver.collectAsState()
    val isValid by viewModel.isValid.collectAsState()
    val blockTimer by viewModel.blockTimer.collectAsState()
    val isUnblocked by viewModel.isUnblocked.collectAsState()
    val textList = viewModel.textList

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 2.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Leading Icon",
                    tint = PrimaryText,
                    modifier = Modifier
                        .size(42.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) { onNavigateToSignIn() }
                )
                Text(
                    text = "Код подтверждения",
                    modifier = Modifier
                        .padding(start = 2.dp),
                    fontSize = 17.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = PrimaryText,
                )
            }
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 20.dp),
        ) {
            Text(
                text = "На электронную почту ${viewModel.userEmail.value} было отправлено письмо с кодом подтверждения",
                modifier = Modifier,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = PrimaryText,
            )
            Spacer(modifier = Modifier.height(20.dp))
            VerificationCodeFields(
                isValid = isValid,
                viewModel = viewModel,
                textList = textList,
                isUnblocked = isUnblocked
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (isValid == false || !isUnblocked) {
                val text = if (!isUnblocked) "Слишком много попыток" else "Код введён не верно"
                Text(
                    text = text,
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = Color.Red,
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
            if (!isUnblocked) {
                Text(
                    text = "Повторно отправить код через: 0${blockTimer / 60}:${if (blockTimer % 60 >= 10) "${blockTimer % 60}" else "0${blockTimer % 60}"}",
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    color = SecondaryText,
                )
            } else {
                if (isOver) {
                    Text(
                        text = "Повторно отправить код",
                        modifier = Modifier.clickable(interactionSource =  MutableInteractionSource(),
                            indication = null) { viewModel.resendCode() },
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        color = Color.Blue,
                    )
                } else {
                    Text(
                        text = "Повторно отправить код через: 00:${if (timer >= 10) "$timer" else "0$timer"}",
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                        color = SecondaryText,
                    )
                }
            }
        }
        Button(
            onClick = {
                viewModel.metricClick(DataClickMetric(Buttons.VERIFY_CODE, Screens.Tutorial.route))
                onNavigateToTutorial()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 42.dp, vertical = 48.dp),
            shape = RoundedCornerShape(50.dp),
            enabled = (isValid == true),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = LightYellowBtn,
                contentColor = PrimaryText,
                disabledBackgroundColor = DisabledBtnBg,
                disabledContentColor = DisabledBtnText
            ),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Text(
                text = "Продолжить", fontSize = 16.sp,
                fontFamily = Roboto
            )
        }
    }
}

@Composable
private fun InputField(
    value: TextFieldValue,
    borderColor: Color,
    onValueChange: (value: TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    isUnblocked: Boolean
) {
    BasicTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        maxLines = 1,
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(10.dp))
            .background(VerificationCodeBg)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .focusRequester(focusRequester),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
            ) {
                innerTextField()
            }
        },
        cursorBrush = SolidColor(PrimaryText),
        textStyle = TextStyle(
            fontSize = 20.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = null
        ),
        enabled = isUnblocked
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun VerificationCodeFields(
    isValid: Boolean?,
    viewModel: VerificationCodeViewModel,
    textList: List<TextFieldValue>,
    isUnblocked: Boolean
) {
    val borderColor = if (isValid == null || isValid == true) Color.Transparent else Color.Red
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(300)
        viewModel.requestList[0].requestFocus()
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        for (i in textList.indices) {
            InputField(
                value = textList[i],
                borderColor = borderColor,
                onValueChange = { newValue ->
                    if (textList[i].text.isNotBlank()) {
                        if (newValue.text == "") {
                            viewModel.changeTextListItem(
                                i,
                                TextFieldValue(
                                    text = "",
                                    selection = TextRange(0)
                                )
                            )
                        }
                        return@InputField
                    }
                    viewModel.changeTextListItem(
                        i,
                        TextFieldValue(
                            text = newValue.text,
                            selection = TextRange(newValue.text.length)
                        )
                    )
                    viewModel.connectInputtedCode {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        if (!it) {
                            for (text in textList.indices) {
                                val value = TextFieldValue(
                                    text = "",
                                    selection = TextRange(0)
                                )
                                viewModel.changeTextListItem(text, value)
                            }
                        }
                    }
                    viewModel.nextFocus()
                },
                focusRequester = viewModel.requestList[i],
                isUnblocked = isUnblocked
            )
            if (i != textList.size - 1) {
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }
}

