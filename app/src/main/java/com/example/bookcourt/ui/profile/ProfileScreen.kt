package com.example.bookcourt.ui

import android.content.ContentResolver
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.ui.auth.SignInViewModel
import com.example.bookcourt.ui.profile.ProfileViewModel
import com.example.bookcourt.ui.theme.CustomButton
import com.example.bookcourt.ui.theme.LightYellowBtn
import com.example.bookcourt.ui.theme.MainBgColor

@Composable
fun ProfileScreen(
    onNavigateToRecommendation: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MainBgColor)
            .verticalScroll(rememberScrollState())
    ) {
        ProfileTopSection(Modifier.height(64.dp), onNavigateToRecommendation)
        Spacer(modifier = Modifier.height(60.dp))
        ProfileMainSection(viewModel)
        Spacer(modifier = Modifier.height(60.dp))
        ProfileBottomSection()
    }
}

@Composable
fun ProfileTopSection(modifier: Modifier, onNavigateToRecommendation: () -> Unit) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(horizontal = 17.dp)
                .align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.clickable {
                    onNavigateToRecommendation()
                },
                painter = painterResource(id = R.drawable.arrow_left),
                contentDescription = "arrow_left"
            )
            Spacer(modifier = Modifier.width(16.dp))
            TextRobotRegular(
                text = stringResource(R.string.profile_screen_profile),
                color = Color.Black,
                fontSize = 18,
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(DarkBgColor)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun ProfileMainSection(
    viewModel: ProfileViewModel
) {
    var resources = LocalContext.current.resources

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            viewModel.onProfileImageChanged(uri)
        }

    var imagePlaceholderUri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(R.drawable.image_placeholder))
        .appendPath(resources.getResourceTypeName(R.drawable.image_placeholder))
        .appendPath(resources.getResourceEntryName(R.drawable.image_placeholder))
        .build()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .clickable {
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

        ProfileOutlinedTextField(
            viewModel.name,
            { viewModel.onNameChanged(it) },
            stringResource(R.string.profile_screen_name)
        )

        Spacer(modifier = Modifier.height(24.dp))
        ProfileOutlinedTextField(
            viewModel.email,
            { viewModel.onEmailChanged(it) },
            stringResource(R.string.profile_screen_email)
        )
        Spacer(modifier = Modifier.height(24.dp))
//        ProfileOutlinedTextField(
//            name,
//            {name = it},
//            stringResource(R.string.profile_screen_city)
//        )
        Spacer(modifier = Modifier.height(24.dp))
//        ProfileOutlinedTextField(
//            name,
//            {name = it},
//            stringResource(R.string.profile_screen_birthday_date)
//        )
        Spacer(modifier = Modifier.height(8.dp))
        SexCheckBox(viewModel)
        Spacer(modifier = Modifier.height(40.dp))
        Column() {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
            ) {
                Row() {
                    TextRobotRegular(
                        text = stringResource(R.string.profile_screen_push_notification),
                        color = DarkGreyColor,
                        fontSize = 16,
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    TextRobotRegular(
                        text = stringResource(R.string.profile_screen_soon),
                        color = linkColor,
                        fontSize = 16,
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_unenabled_arrow_dropdown),
                    contentDescription = null
                )
            }
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(DarkBgColor)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
            ) {
                TextRobotRegular(
                    text = stringResource(R.string.profile_screen_report_the_problem),
                    color = Color.Black,
                    fontSize = 16,
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_enabled_arrow_dropdown),
                    contentDescription = null
                )
            }
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(DarkBgColor)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
            ) {
                TextRobotRegular(
                    text = stringResource(R.string.profile_screen_legal_information),
                    color = Color.Black,
                    fontSize = 16,
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_enabled_arrow_dropdown),
                    contentDescription = null
                )
            }
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(DarkBgColor)
            )
        }

    }

}

@Composable
fun SexCheckBox(viewModel: ProfileViewModel) {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
        TextRobotRegular(
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
                .background(if (viewModel.sex != MALE) MainBgColor else DarkBgColor)
                .clickable {
                    viewModel.onSexChanged(MALE)
                },
            contentAlignment = Alignment.Center
        ) {
            TextRobotRegular(
                text = MALE,
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
                .background(if (viewModel.sex != FEMALE) MainBgColor else DarkBgColor)
                .clickable {
                    viewModel.onSexChanged(FEMALE)
                },
            contentAlignment = Alignment.Center
        ) {
            TextRobotRegular(
                text = FEMALE,
                color = Color.Black,
                fontSize = 14,
            )
        }
    }
}

@Composable
fun ProfileBottomSection() {
    Column(
        modifier = Modifier.padding(horizontal = 17.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomButton(
            text = stringResource(R.string.profile_screen_save),
            textColor = Color.Black,
            color = LightYellowBtn,
            onCLick = {}
            //TODO
        )
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50.dp))
                .background(color = DarkBgColor)
                .padding(vertical = 13.dp, horizontal = 20.dp)
                .clickable { }, //TODO
            Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.google_play_logo),
                    contentDescription = null,
                    modifier = Modifier.clickable { }  //TODO
                )
                Spacer(Modifier.width(12.dp))
                TextRobotRegular(
                    text = stringResource(R.string.profile_screen_rate_us),
                    color = Color.Black,
                    fontSize = 16,
                )
            }

        }
        Spacer(Modifier.height(24.dp))
        TextRobotRegular(
            text = stringResource(R.string.profile_screen_logOut),
            color = logOutColor,
            fontSize = 16,
        )
        Spacer(Modifier.height(42.dp))
        TextRobotRegular(
            stringResource(R.string.profile_screen_version),
            DarkGreyColor,
            14
        )
        Spacer(Modifier.height(15.dp))
    }
}

@Composable
fun ProfileOutlinedTextField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    paddingStart: Dp,
    height: Dp,
    trailingIcon: Int,
) {
    val lineHeightSp: TextUnit = 12.sp
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
            ) {
                TextRobotRegular(text = label, color = DarkGreyColor, fontSize = 12)
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
                modifier = Modifier.fillMaxSize().padding(end = paddingStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = { onValueChanged(it) },
                    modifier = Modifier.padding(start = paddingStart),
                    singleLine = true
                )
                Image(painter = painterResource(id = trailingIcon), contentDescription = null)
            }

        }
    }
}

@Preview
@Composable
fun ProfileOutlinedTextFieldPreview() {
    var testValue = remember {
        mutableStateOf<String>("jopa")
    }
    ProfileOutlinedTextField(
        "Test",
        value = testValue.value,
        onValueChanged = { testValue.value = it },
        16.dp,
        48.dp,
        R.drawable.arrow_down
    )
}

//    @Preview(showBackground = true)
//    @Composable
//    fun ProfileScreenPreview() {
//        ProfileScreen({})
//    }

@Composable
fun TextRobotRegular(text: String, color: Color, fontSize: Int) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize.sp,
        fontFamily = FontFamily(
            Font(
                R.font.roboto_regular
            )
        )
    )
}

@Composable
fun ProfileOutlinedTextField(value: String, onValueChanged: (String) -> Unit, label: String) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = { onValueChanged(it) },
        label = { Text(text = label) },
        singleLine = true,
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = BorderColor,
            focusedLabelColor = DarkGreyColor,
            cursorColor = Color.Black
        ),
    )
}

val DarkBgColor = Color(239, 235, 222)
val BorderColor = Color(217, 217, 217)
val DarkGreyColor = Color(140, 140, 140)
val linkColor = Color(14, 125, 255)
val logOutColor = Color(252, 87, 59)
const val MALE = "Мужской"
const val FEMALE = "Женский"


