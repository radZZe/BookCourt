package com.example.bookcourt.ui.theme

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.example.bookcourt.utils.Constants.OTHER_CITY
import com.example.bookcourt.utils.Constants.cities
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
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
            .clip(RoundedCornerShape(50.dp))
            .background(color = color)
            .padding(vertical = 13.dp, horizontal = 20.dp)
            .clickable { onCLick() },
        Alignment.Center
    ) {
        Text(text = text, color = textColor, fontSize = 16.sp, fontFamily = Roboto)
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
    text: String = "Заглянуть в магазин"
) {
    Button(
        onClick = {
            val sendIntent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
            val webIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(webIntent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 13.dp, horizontal = 20.dp)
            .clip(RoundedCornerShape(60.dp))
            .height(46.dp),
        colors = ButtonDefaults.buttonColors(color)
    ) {
        Text(text = text, fontSize = 16.sp, fontFamily = Roboto)
    }
}




