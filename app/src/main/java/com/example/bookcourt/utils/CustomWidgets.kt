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
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import com.example.bookcourt.R
import com.example.bookcourt.ui.statistics.*
import com.example.bookcourt.utils.BitmapUtils
import com.example.bookcourt.utils.Constants
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

@Composable
fun CityItem(
    fontSize:Int,
    title: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
            }
            .padding(start = 14.dp, end = 14.dp, )
    ) {
        Text(
            text = title,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_regular
                )
            ),
            fontSize = fontSize.sp,
            color = Color.Black,
        )
    }
}

@Composable
fun CityDropDownMenu(
    value: String,
    onTFValueChange: (String) -> Unit,
    textWrapper:@Composable (innerTextField:@Composable ()-> Unit)->Unit,
    fontSize:Int,
    itemsFontSize:Int,
    backgroundColor: Color,
) {

    var textFieldValue by remember {
        mutableStateOf(value)
    }

    var isAvailable by remember {
        mutableStateOf(false)
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


    BasicTextField(
        value = textFieldValue,
        enabled = isAvailable,
        onValueChange = {
            textFieldValue = it
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
            .focusRequester(focusRequester)
            .size(48.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        textStyle = TextStyle(
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_regular
                )
            ),
            fontSize = fontSize.sp,
            color = Color.Black
        )
    ) { innerTextField ->
        textWrapper(innerTextField)

    }
    AnimatedVisibility(visible = expanded) {
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(15.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 150.dp)
                    .zIndex(2f)
                    .background(backgroundColor)
            ) {
                items(
                    if (isAvailable) {
                        Constants.cities.apply {
                            filterNot {
                                it.contains(Constants.OTHER_CITY)
                            }
                            filter {
                                it.lowercase()
                                    .contains(textFieldValue.lowercase()) || it.lowercase()
                                    .contains("others")
                            }
                            sorted()
                        }
                    } else {
                        Constants.cities.sorted().also {
                            Collections.swap(it, it.indexOf(Constants.OTHER_CITY), it.lastIndex)
                        }
                    }

                ) {
                    CityItem(title = it, fontSize = itemsFontSize) { title ->
                        if (title == Constants.OTHER_CITY) {
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




