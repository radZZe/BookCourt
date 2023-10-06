package com.example.bookcourt.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.utils.TextRobotoRegular


@Composable
fun AskQuestionScreen(
    onNavigateToSupport: () -> Unit = {},
    viewModel: AskQuestionViewModel = hiltViewModel()
) {
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBgColor)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Arrow Back",
                    modifier = Modifier
                        .size(38.dp)
                        .clickable(interactionSource =  MutableInteractionSource(),
                            indication = null) { onNavigateToSupport() }
                )
                Spacer(modifier = Modifier.width(10.dp))
                TextRobotoRegular(
                    text = "Задать вопрос",
                    color = Color.Black,
                    fontSize = 18,
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_confirm),
                contentDescription = "Confirm",
                modifier = Modifier

                    .clickable(interactionSource =  MutableInteractionSource(),
                        indication = null) { }
                    .padding(end = 16.dp)
                    .size(22.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(DarkBgColor)
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextRobotoRegular(
                text = "Подробное описание позволит нам предоставить ответ в" +
                        " кратчайшее сроки без уточнения дополнительной информации.",
                color = Color.Black,
                fontSize = 15
            )
            Spacer(modifier = Modifier.height(16.dp))
            QuestionTextField(viewModel)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.wrapContentSize().clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                      },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clip),
                        contentDescription = "Clip Icon",
                        tint = Color.Blue,
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                    TextRobotoRegular(text = "Прикрепить файл", color = Color.Blue, fontSize = 14)
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "Info Icon"
                )
            }
        }
    }
}

@Composable
fun QuestionTextField(
    viewModel: AskQuestionViewModel
) {
    TextField(
        value = viewModel.question,
        onValueChange = { viewModel.onQuestionChanged(it) },
        maxLines = 13,
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = DarkBgColor,
            cursorColor = Color.Black,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    )
}