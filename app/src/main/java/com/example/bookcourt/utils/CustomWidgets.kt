package com.example.bookcourt.ui.theme

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import com.example.bookcourt.R
import com.example.bookcourt.utils.Constants.cities

@Composable
fun TutorialGreeting(
    onCLick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Как работают свайпы?",
                fontSize = 26.sp,
                fontFamily = Gilroy,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_swipe_up),
                    contentDescription = "",
                    tint = Brown
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Добавить в желаемое",
                    fontSize = 22.sp,
                    fontFamily = Gilroy,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_swipe_right),
                    contentDescription = "",
                    tint = Brown
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Нравится",
                    fontSize = 22.sp,
                    fontFamily = Gilroy,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_swipe_left),
                    contentDescription = "",
                    tint = Brown
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Не нравится",
                    fontSize = 22.sp,
                    fontFamily = Gilroy,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_swipe_down),
                    contentDescription = "",
                    tint = Brown
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Пропустить",
                    fontSize = 22.sp,
                    fontFamily = Gilroy,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomButton(
                    text = "Хорошо",
                    color = BrightOlive,
                    textColor = Color.White
                ) { onCLick() }
            }
        }
    }
}

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
    text: String,
    textColor: Color = LightBrown,
    color: Color = Brown,
    onCLick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .background(color = color)
            .padding(top = 12.dp, bottom = 12.dp, start = 20.dp, end = 20.dp)
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

    val heightTextFields by remember {
        mutableStateOf(55.dp)
    }

    var textFieldsSize by remember {
        mutableStateOf(Size.Zero)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

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
        onValueChange = {
            onTFValueChange(it)
//            textFieldValue = it
            expanded = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                textFieldsSize = it.size.toSize()
            },
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
            IconButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = "arrow"
                )
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
                if (textFieldValue.isNotEmpty()) {
                    items(
                        cities.filter {
                            it.lowercase()
                                .contains(textFieldValue.lowercase()) || it.lowercase()
                                .contains("others")
                        }
                            .sorted()
                    ) {
                        CityItem(title = it) { title ->
                            textFieldValue = title
                            onTFValueChange(title)
                            expanded = false
                        }
                    }
                } else {
                    items(
                        cities.sorted()
                    ) {
                        CityItem(title = it) { title ->
                            textFieldValue = title
                            onTFValueChange(title)
                            expanded = false
                        }
                    }
                }
            }
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


