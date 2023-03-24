package com.example.bookcourt.ui.theme

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import com.example.bookcourt.R
import com.example.bookcourt.ui.statistics.IgraSlov
import com.example.bookcourt.ui.statistics.Zarya
import com.example.bookcourt.utils.Constants.OTHER_CITY
import com.example.bookcourt.utils.Constants.cities
import com.example.bookcourt.utils.Constants.statisticScreensList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Collections


@Composable
fun CustomCheckBox(
    text: String,
    value: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = value,
            onCheckedChange = { onCheckedChange(it) },
            colors = CheckboxDefaults.colors(
                checkedColor = colorResource(id = R.color.main_color),
                uncheckedColor = Color.Gray,
            )
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.Gray,
        )
    }
}

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
            ClearIconBtn(isClearIconBtnVisible){
                textFieldValue = ""
                onTFValueChange(textFieldValue)
                isAvailable = false
                focusManager.clearFocus()
                isClearIconBtnVisible = false
            }
        }
    )
    AnimatedVisibility(visible = expanded) {
        Card(
            modifier = Modifier
//                .padding(horizontal = 5.dp)
//                .width(textFieldsSize.width.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 150.dp)
                    .zIndex(2f)
            ) {
                    items(
                        if (isAvailable){
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
                        }
                        else{
                            cities.sorted().also {
                                    Collections.swap(it,it.indexOf(OTHER_CITY),it.lastIndex)
                            }
                        }

                    ) {
                        CityItem(title = it) { title ->
                            if (title == OTHER_CITY){
                                isAvailable = true
                               // isClearIconBtnVisible = true
                                focusRequester.requestFocus()
                                textFieldValue = ""
                                onTFValueChange(textFieldValue)
                            }
                            else{
                                onTFValueChange(title)
                                textFieldValue = title
                                onTFValueChange(textFieldValue)
                            }
                            isClearIconBtnVisible = true
                            expanded = false
                        }
                        if (isAvailable){
                            focusRequester.requestFocus()
                        }
                    }
            }
        }
    }
}

@Composable
fun ClearIconBtn(isVisible:Boolean, onClick:()->Unit) {
    if (isVisible){
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
    onAnimationEnd: () -> Unit
) {

    var progress by remember {
        mutableStateOf(0.00f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    if (startProgress) {
        LaunchedEffect(key1 = Unit) {
            while (progress < 1f) {
                progress += 0.01f
                delay(50)
            }
            onAnimationEnd() // The action after the timeline is over
        }
    }

    LinearProgressIndicator(
        backgroundColor = Color.LightGray,
        color = Color.White,
        modifier = modifier
            .padding(top = 12.dp, bottom = 12.dp)
            .clip(RoundedCornerShape(12.dp)),
        progress = animatedProgress
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun StoryLikePages(
    pagerState: PagerState,
    screens: List<String>
) {
    HorizontalPager(state = pagerState, dragEnabled = false) { page ->
        when(screens[page]) {
            "IgraSlov" -> { IgraSlov() }
            else -> { Zarya() }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
@Preview
fun Statistics() {
    // n - number of "stories" in stats screen < change it on your own
    val stories = statisticScreensList.size
    val pagerState = rememberPagerState(pageCount = stories)
    val coroutineScope = rememberCoroutineScope()

    var currentPage by remember {
        mutableStateOf(0)
    }

    val darkGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF14161A), Color(0xFF2E3643), Color(0xFF303845)
        )
    )
    Box(modifier = Modifier.fillMaxSize()) {
        StoryLikePages(pagerState = pagerState, statisticScreensList)
        Box(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
//            Box(modifier = Modifier.height(24.dp).fillMaxWidth().zIndex(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth().zIndex(2f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.padding(4.dp))
                for( i in 0 until stories) {
                    InstaStoriesProgressBar(
                        modifier = Modifier.weight(1f),
                        startProgress = (i == currentPage)) {
                        coroutineScope.launch {
                            if (currentPage < stories - 1) {
                                currentPage++
                            }
                            pagerState.animateScrollToPage(currentPage)
                        }
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }

    }
}


