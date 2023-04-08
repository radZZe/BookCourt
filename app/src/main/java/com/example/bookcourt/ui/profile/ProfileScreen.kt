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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.CustomButton
import com.example.bookcourt.ui.theme.LightYellowBtn
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.ui.theme.Roboto

@Composable
fun ProfileScreen(onNavigateToRecommendation: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MainBgColor)
            .verticalScroll(rememberScrollState())
    ) {
        ProfileTopSection(Modifier.height(64.dp), onNavigateToRecommendation)
        Spacer(modifier = Modifier.height(60.dp))
        ProfileMainSection()
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
            Text(
                text = "Профиль",
                fontSize = 18.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.roboto_regular
                    )
                ),
                color = Color.Black
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(239, 235, 222))
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun ProfileMainSection() {

    //TODO move to viewModel
    var selectedImage by remember {
        mutableStateOf<Uri?>(null)
    }

    var name by remember {
        mutableStateOf<String>("")
    }

    var resources = LocalContext.current.resources

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            selectedImage = uri
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
                model = if (selectedImage == null) imagePlaceholderUri else selectedImage,
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

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Имя") },
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_close_circle),
                    contentDescription = null
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(217, 217, 217),
                focusedLabelColor = Color(140, 140, 140),
                cursorColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Электронная почта") },
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_close_circle),
                    contentDescription = null
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(217, 217, 217),
                focusedLabelColor = Color(140, 140, 140),
                cursorColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Город") },
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.arrow_down),
                    contentDescription = null
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(217, 217, 217),
                focusedLabelColor = Color(140, 140, 140),
                cursorColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Дата рождения") },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(217, 217, 217),
                focusedLabelColor = Color(140, 140, 140),
                cursorColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = true,
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        // TODO move to viewmodel
        var checkState = remember { mutableStateOf<String?>(null) }
        SexCheckBox(checkState)
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
                    Text(
                        text = "Пуш-уведомления",
                        color = Color(140, 140, 140),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular
                            )
                        )
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Скоро",
                        color = Color(14, 125, 255),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_regular
                            )
                        )
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
                    .background(Color(239, 235, 222))
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Сообщить о проблеме",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.roboto_regular
                        )
                    )
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
                    .background(Color(239, 235, 222))
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Правовая информация",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.roboto_regular
                        )
                    )
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
                    .background(Color(239, 235, 222))
            )
        }

    }

}

@Composable
fun SexCheckBox(state: MutableState<String?>) {
    Row(horizontalArrangement = Arrangement.Start,modifier =Modifier.fillMaxWidth()){
        Text(
            text = "Пол",
            fontSize = 14.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_regular
                )
            ),
            color = Color(140, 140, 140)
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
                    1.dp, Color(217, 217, 217), AbsoluteRoundedCornerShape(
                        topLeft = 15.dp,
                        bottomLeft = 15.dp,
                        topRight = 0.dp,
                        bottomRight = 0.dp
                    )
                )
                .background(if (state.value != "Мужской") MainBgColor else Color(239, 235, 222))
                .clickable {
                    state.value = "Мужской"
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier,
                text = "Мужской",
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = FontFamily(
                    Font(
                        R.font.roboto_regular
                    )
                )
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
                    1.dp, Color(217, 217, 217), AbsoluteRoundedCornerShape(
                        topLeft = 0.dp,
                        bottomLeft = 0.dp,
                        topRight = 15.dp,
                        bottomRight = 15.dp
                    )
                )
                .background(if (state.value != "Женский") MainBgColor else Color(239, 235, 222))
                .clickable {
                    state.value = "Женский"
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Женский",
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = FontFamily(
                    Font(
                        R.font.roboto_regular
                    )
                )
            )
        }
    }
}

@Composable
fun ProfileBottomSection() {
    Column(modifier = Modifier.padding(horizontal = 17.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        CustomButton(
            text = "Сохранить",
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
                .background(color = Color(239, 235, 222))
                .padding(vertical = 13.dp, horizontal = 20.dp)
                .clickable { }, //TODO
            Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.Center,modifier = Modifier.fillMaxWidth()){
                Image(
                    painter = painterResource(id = R.drawable.google_play_logo),
                    contentDescription = null,
                    modifier = Modifier.clickable {  }  //TODO
                )
                Spacer(Modifier.width(12.dp))
                Text(text = "Оценить нас", color = Color.Black, fontSize = 16.sp, fontFamily = Roboto)
            }

        }
        Spacer(Modifier.height(24.dp))
        Text(
            text="Выйти",
            color= Color(252, 87, 59),
            fontSize =16.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_regular
                )
            )
        )
        Spacer(Modifier.height(42.dp))
        Text(
            text="BookCourt v1.1",
            color= Color(140, 140, 140),
            fontSize =14.sp,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_regular
                )
            )
        )
        Spacer(Modifier.height(15.dp))
    }
}


    @Preview(showBackground = true)
    @Composable
    fun ProfileScreenPreview() {
        ProfileScreen({})
    }



