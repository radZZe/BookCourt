package com.example.bookcourt.ui.theme

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import com.example.bookcourt.ui.statistics.*
import com.example.bookcourt.utils.BitmapUtils
import com.example.bookcourt.utils.Constants.OTHER_CITY
import com.example.bookcourt.utils.Constants.cities
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.delay
import java.util.*


@Composable
fun CustomButton(
    text: String = "Text",
    textColor: Color = LightBrown,
    color: Color = Brown,
    modifier: Modifier = Modifier,
    onCLick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(60.dp))
            .background(color = color)
            .padding(vertical = 13.dp, horizontal = 20.dp)
            .clickable { onCLick() },
        Alignment.Center
    ) {
        Text(text = text, color = textColor, fontSize = 16.sp, fontFamily = Gilroy)
    }
}

@Composable
fun AutoCompleteTextField(
    label: String,
    placeholder: String,
    value: String,
    onTFValueChange: (String) -> Unit
) {

    var textFieldValue by remember {
        mutableStateOf(value)
    }

    var isAvailable by remember {
        mutableStateOf(false)
    }

    val heightTextFields by remember {
        mutableStateOf(55.dp)
    }

    var textFieldsSize by remember {
        mutableStateOf(Size.Zero)
    }

    var isClearIconBtnVisible by remember {
        mutableStateOf(false)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = {
                expanded = false
            }
        )
    ) {
        Text(
            text = label,
            fontFamily = Gilroy,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    TextField(
        value = textFieldValue,
        enabled = isAvailable,
        onValueChange = {
            textFieldValue = it
            //expanded = true
            onTFValueChange(it)
            expanded = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = true),
                onClick = { expanded = !expanded }
            )
            .onGloballyPositioned {
                textFieldsSize = it.size.toSize()
            }
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.textFieldColors(
            placeholderColor = TextPlaceHolderColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            backgroundColor = TextFieldGrey,
            cursorColor = Brown
        ),
        maxLines = 1,
        placeholder = {
            Text(
                text = placeholder,
                color = TextPlaceHolderColor,
                fontFamily = Gilroy
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        textStyle = TextStyle(
            fontFamily = Gilroy,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        ),
        singleLine = true,
        trailingIcon = {
            ClearIconBtn(isClearIconBtnVisible) {
                textFieldValue = ""
                onTFValueChange(textFieldValue)
                isAvailable = false
                focusManager.clearFocus()
                isClearIconBtnVisible = false
            }
        }
    )
    AnimatedVisibility(visible = expanded) {
        Card(modifier = Modifier) {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 150.dp)
                    .zIndex(2f)
            ) {
                items(
                    if (isAvailable) {
                        cities.apply {
                            filterNot {
                                it.contains(OTHER_CITY)
                            }
                            filter {
                                it.lowercase()
                                    .contains(textFieldValue.lowercase()) || it.lowercase()
                                    .contains("others")
                            }
                            sorted()
                        }
                    } else {
                        cities.sorted().also {
                            Collections.swap(it, it.indexOf(OTHER_CITY), it.lastIndex)
                        }
                    }

                ) {
                    CityItem(title = it) { title ->
                        if (title == OTHER_CITY) {
                            isAvailable = true
                            // isClearIconBtnVisible = true
                            focusRequester.requestFocus()
                            textFieldValue = ""
                            onTFValueChange(textFieldValue)
                        } else {
                            onTFValueChange(title)
                            textFieldValue = title
                            onTFValueChange(textFieldValue)
                        }
                        isClearIconBtnVisible = true
                        expanded = false
                    }
                    if (isAvailable) {
                        focusRequester.requestFocus()
                    }
                }
            }
        }
    }
}

@Composable
fun ClearIconBtn(isVisible: Boolean, onClick: () -> Unit) {
    if (isVisible) {
        IconButton(
            onClick = { onClick() },
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "arrow"
            )
        }
    }
}

@Composable
fun CityItem(
    title: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
            }
            .padding(10.dp)
    ) {
        Text(
            text = title, fontFamily = Gilroy,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun InstaStoriesProgressBar(
    modifier: Modifier,
    startProgress: Boolean = false,
    paused: Boolean = false,
    isNext: Boolean,
    onAnimationEnd: () -> Unit,

) {
    val percent = remember { Animatable(0f) }

    if (startProgress) {
        LaunchedEffect(paused) {
            percent.snapTo(0f)
            if (paused) percent.stop()
            else {
                percent.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = (5000 * (1f - percent.value)).toInt(), // (3)
                        easing = LinearEasing
                    )
                )
                onAnimationEnd()
            }
        }
    } else {
        LaunchedEffect(paused) {
            if (isNext) {
                percent.snapTo(0f)
            } else {
                percent.snapTo(1f)
            }
        }
    }

    LinearProgressIndicator(
        backgroundColor = Color.LightGray,
        color = Color.White,
        modifier = modifier,
        progress = percent.value
    )
}

@Composable
fun RedirectButton(
    context: Context,
    redirectUrl: String,
    color: Color = LightYellowBtn,
    text: String = "Заглянуть в магазин",
    modifier: Modifier = Modifier
) {

    Button(
        onClick = {
            val sendIntent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
            val webIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(webIntent)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 13.dp, horizontal = 20.dp)
            .clip(RoundedCornerShape(60.dp))
            .height(45.dp),
        colors = ButtonDefaults.buttonColors(color)
    ) {
        Text(text = text)
    }
}

@Composable
fun ShareStatisticsButton(
    context: Context,
    contentView:View,
    color: Color = LightYellowBtn,
    text: String = "Поделиться",
    modifier: Modifier = Modifier
) {

    Button(
        onClick = {
            val bitmap = Bitmap.createBitmap(
                contentView.width,
                contentView.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            contentView.layout(
                contentView.left,
                contentView.top,
                contentView.right,
                contentView.bottom
            )
            contentView.draw(canvas)

            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_STREAM,
                    BitmapUtils.getBitmapUri(
                        context,
                        bitmap,
                        "statistics",
                        "images/"
                    )
                )
                type = "image/jpeg"
            }
            context.startActivity(Intent.createChooser(shareIntent, null))
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 13.dp, horizontal = 20.dp)
            .clip(RoundedCornerShape(60.dp))
            .height(45.dp),
        colors = ButtonDefaults.buttonColors(color)
    ) {
        Text(text = text)
    }
}




