package com.example.bookcourt.ui.profile

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.models.user.Sex
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.utils.CustomButton
import com.example.bookcourt.utils.TextRobotoRegular
import java.util.*


@Composable
fun ProfileSettings(
    onNavigateToProfile: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    viewModel: ProfileSettingsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit, block = {
        viewModel.getUser()
    })
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MainBgColor)) {
        ScreenHeader(
            text = "Настройки профиля",
            goBack = { onNavigateToProfile() }
        )
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.height(26.dp))
                ProfileMainSection(
                    { onNavigateToSignIn() },
                    viewModel
                )
                CustomButton(
                    text = "Сохранить",
                    textColor = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    onCLick = {
                        viewModel.saveUserData()
                    }
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                        .height(26.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) { viewModel.logOutUser { onNavigateToProfile() } }
                ) {
                    TextRobotoRegular(
                        text = stringResource(R.string.profile_screen_logOut),
                        color = logOutColor,
                        fontSize = 14,
                    )
                }

            }
            SimpleSnackBar(
                text = "Успешно",
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = viewModel.isVisibleSnackBar
            )

        }
    }


}

@Composable
fun ProfileMainSection(
    onNavigateToSignIn: () -> Unit,
    viewModel: ProfileSettingsViewModel
) {

    val resources = LocalContext.current.resources
    val resolver = LocalContext.current.contentResolver

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                resolver.takePersistableUriPermission(uri, flags)
                viewModel.onProfileImageChanged(uri)
            }
        }


    val imagePlaceholderUri = remember {
        Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.image_placeholder))
            .appendPath(resources.getResourceTypeName(R.drawable.image_placeholder))
            .appendPath(resources.getResourceEntryName(R.drawable.image_placeholder))
            .build()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            .size(138.dp)
            .clip(CircleShape))
        {
            AsyncImage(
                model = if (viewModel.profileImage == null) imagePlaceholderUri else viewModel.profileImage,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentDescription = "profile_placeholder",
                contentScale = ContentScale.Crop
            )
            Image(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = R.drawable.image_upload_icon),
                contentDescription = "button_image_upload"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        ProfileOutlinedTextField(
            label = "Никнейм",
            value = viewModel.nickname,
            onValueChanged = { viewModel.onNicknameChanged(it) },
            paddingStart = 16.dp,
            height = 48.dp,
            labelFontSize = 13,
            valueFontSize = 16,
            trailingIcon = R.drawable.ic_close_circle,
            onClickTrailingIcon = {
                viewModel.onNameChanged("")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileOutlinedTextField(
            label = stringResource(R.string.profile_screen_name),
            value = viewModel.name,
            onValueChanged = { viewModel.onNameChanged(it) },
            paddingStart = 16.dp,
            height = 48.dp,
            labelFontSize = 13,
            valueFontSize = 16,
            trailingIcon = R.drawable.ic_close_circle,
            onClickTrailingIcon = {
                viewModel.onNameChanged("")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileOutlinedTextField(
            label = stringResource(R.string.profile_screen_surname),
            value = viewModel.surname,
            onValueChanged = { viewModel.onSurnameChanged(it) },
            paddingStart = 16.dp,
            height = 48.dp,
            labelFontSize = 13,
            valueFontSize = 16,
            trailingIcon = R.drawable.ic_close_circle,
            onClickTrailingIcon = {
                viewModel.onNameChanged("")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        EmailField(
            label = stringResource(R.string.profile_screen_email),
            value = viewModel.email,
            paddingStart = 16.dp,
            height = 48.dp,
            labelFontSize = 13,
            valueFontSize = 16,
            onClick = { onNavigateToSignIn() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProfileDatePicker(
            labelFontSize = 13,
            height = 48.dp,
            paddingStart = 16.dp,
            label = stringResource(R.string.profile_screen_birthday_date),
            value = viewModel.date,
            fontSize = 16,
            onDateChanged = {
                viewModel.onBDayDateChanged(it)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        SexCheckBox(viewModel)
        Spacer(modifier = Modifier.height(10.dp))
    }

}

@Composable
fun ProfileDatePicker(
    labelFontSize: Int,
    height: Dp,
    paddingStart: Dp,
    label: String,
    value: String,
    fontSize: Int,
    onDateChanged: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            onDateChanged("$selectedDay.$selectedMonth.$selectedYear")
        }, year, month, day
    )

    val lineHeightSp: TextUnit = labelFontSize.sp
    val lineHeightDp: Dp = with(LocalDensity.current) {
        lineHeightSp.toDp()
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(height)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                datePickerDialog.show()
            }
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(3f)
        ) {
            Spacer(modifier = Modifier.width(paddingStart))
            Box(
                modifier = Modifier
                    .background(MainBgColor)
                    .zIndex(4f)
                    .padding(horizontal = 2.dp)
            ) {
                TextRobotoRegular(text = label, color = DarkGreyColor, fontSize = labelFontSize)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height - lineHeightDp.div(1.5f))
                .clip(
                    RoundedCornerShape(9.dp)
                )
                .border(
                    1.dp, BorderColor, RoundedCornerShape(9.dp)
                )
                .background(MainBgColor)
                .align(Alignment.BottomCenter)
                .zIndex(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = paddingStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    fontFamily = FontFamily(
                        Font(R.font.roboto_regular)
                    ),
                    fontSize = fontSize.sp,
                    modifier = Modifier.padding(start = paddingStart)
                )

            }

        }
    }
}

@Composable
fun SexCheckBox(viewModel: ProfileSettingsViewModel) {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
        TextRobotoRegular(
            text = stringResource(R.string.profile_screen_sex),
            color = DarkGreyColor,
            fontSize = 14,
        )
    }

    Row(
        modifier = Modifier
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
                .background(if (viewModel.sex != Sex.MALE) MainBgColor else DarkBgColor)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    viewModel.onSexChanged(Sex.MALE)
                },
            contentAlignment = Alignment.Center
        ) {
            TextRobotoRegular(
                text = stringResource(R.string.profile_screen_male),
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
                .background(if (viewModel.sex != Sex.FEMALE) MainBgColor else DarkBgColor)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    viewModel.onSexChanged(Sex.FEMALE)
                },
            contentAlignment = Alignment.Center
        ) {
            TextRobotoRegular(
                text = stringResource(R.string.profile_screen_female),
                color = Color.Black,
                fontSize = 14,
            )
        }
    }
}

@Composable
fun ProfileOutlinedTextField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    paddingStart: Dp,
    height: Dp,
    trailingIcon: Int? = null,
    onClickTrailingIcon: () -> Unit,
    labelFontSize: Int,
    valueFontSize: Int,
) {
    val lineHeightSp: TextUnit = labelFontSize.sp
    val lineHeightDp: Dp = with(LocalDensity.current) {
        lineHeightSp.toDp()
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(3f)
        ) {
            Spacer(modifier = Modifier.width(paddingStart))
            Box(
                modifier = Modifier
                    .background(MainBgColor)
                    .zIndex(4f)
                    .padding(horizontal = 2.dp)
            ) {
                TextRobotoRegular(text = label, color = DarkGreyColor, fontSize = labelFontSize)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height - lineHeightDp.div(1.5f))
                .clip(
                    RoundedCornerShape(9.dp)
                )
                .border(
                    1.dp, BorderColor, RoundedCornerShape(9.dp)
                )
                .background(MainBgColor)
                .align(Alignment.BottomCenter)
                .zIndex(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = paddingStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = { onValueChanged(it) },
                    modifier = Modifier.padding(start = paddingStart),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = valueFontSize.sp,
                        fontFamily = FontFamily(
                            Font(R.font.roboto_regular)
                        )
                    )
                )
                if (trailingIcon != null) Image(
                    painter = painterResource(id = trailingIcon),
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) { onClickTrailingIcon() })
            }

        }
    }
}

@Composable
fun SimpleSnackBar(
    text: String,
    modifier: Modifier,
    visible: Boolean
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(DarkBgColor)
                    .padding(vertical = 8.dp, horizontal = 20.dp)
            ) {
                Text(text = text)
            }
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun EmailField(
    label: String,
    value: String,
    paddingStart: Dp,
    height: Dp,
    labelFontSize: Int,
    valueFontSize: Int,
    onClick: () -> Unit
) {
    val lineHeightSp: TextUnit = labelFontSize.sp
    val lineHeightDp: Dp = with(LocalDensity.current) {
        lineHeightSp.toDp()
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(height)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(3f)
        ) {
            Spacer(modifier = Modifier.width(paddingStart))
            Box(
                modifier = Modifier
                    .background(MainBgColor)
                    .zIndex(4f)
                    .padding(horizontal = 2.dp)
            ) {
                TextRobotoRegular(text = label, color = DarkGreyColor, fontSize = labelFontSize)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height - lineHeightDp.div(1.5f))
                .clip(RoundedCornerShape(9.dp))
                .border(1.dp, BorderColor, RoundedCornerShape(9.dp))
                .background(MainBgColor)
                .align(Alignment.BottomCenter)
                .zIndex(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = paddingStart)
                    .clickable { onClick() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextRobotoRegular(
                    text = value,
                    color = Color.Black,
                    fontSize = valueFontSize,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}