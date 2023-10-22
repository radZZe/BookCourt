package com.example.bookcourt.ui.profile

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.security.keystore.UserNotAuthenticatedException
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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.ui.theme.Roboto
import com.example.bookcourt.ui.theme.dimens
import com.example.bookcourt.utils.CustomButton
import com.example.bookcourt.utils.TextRobotoRegular


@Composable
fun ProfileScreen(
    onNavigateToSupport: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToStatistics: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {},
    onNavigateToOrdersNotifications: () -> Unit,
    onNavigateToWantToRead: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToCategorySelection: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val isAuthenticated = viewModel.isAuthenticated.collectAsState(initial = false)

    if (isAuthenticated.value) {

        LaunchedEffect(key1 = Unit, block = {
            viewModel.getUser()
        })

        val resources = LocalContext.current.resources

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
                .fillMaxSize()
                .background(MainBgColor)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                    )
                    {
                        AsyncImage(
                            model = if (viewModel.profileImage == null) imagePlaceholderUri else viewModel.profileImage,
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Center),
                            contentDescription = "profile_placeholder",
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "${viewModel.name} ${viewModel.surname}",
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
            StatsHighlights(onNavigateToStatistics = { onNavigateToStatistics() })
            Spacer(modifier = Modifier.height(20.dp))
            ProfileOptions(
                viewModel = viewModel,
                onNavigateToOrdersNotifications = { onNavigateToOrdersNotifications() },
                onNavigateToWantToRead = { onNavigateToWantToRead() },
                onNavigateToOrders = { onNavigateToOrders() }
            )
            Spacer(modifier = Modifier.height(20.dp))
            InfoOptions(
                onNavigateToSettings = { onNavigateToSettings() },
                onNavigateToCategorySelection = { onNavigateToCategorySelection() }
            )
            Spacer(modifier = Modifier.height(20.dp))
            AdminOptions(
                modifier = Modifier.padding(horizontal = 10.dp),
                onNavigateToAbout = { onNavigateToAbout() },
                onNavigateToSupport = { onNavigateToSupport() },
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    } else {
        UserNotAuthenticated(
            onNavigateToSupport = { onNavigateToSupport() },
            onNavigateToAbout = { onNavigateToAbout() },
            onNavigateToSignIn = { onNavigateToSignIn() }
        )
    }

}

@Composable
private fun InfoOptions(
    onNavigateToSettings: () -> Unit,
    onNavigateToCategorySelection: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(DarkBgColor)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        OptionItem(
            icon = R.drawable.ic_setting,
            text = "Настройки профиля",
            onClick = { onNavigateToSettings() })
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 10.dp)
                .background(Color.LightGray)
        )
        OptionItem(
            icon = R.drawable.ic_blank_heart,
            text = "Предпочтения",
            onClick = { onNavigateToCategorySelection() }
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun ProfileOptions(
    viewModel: ProfileViewModel,
    onNavigateToOrdersNotifications: () -> Unit,
    onNavigateToWantToRead: () -> Unit,
    onNavigateToOrders: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(DarkBgColor)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        OptionItem(
            icon = R.drawable.ic_box,
            text = "Заказы",
            onClick = { onNavigateToOrders() },
            notification = { NotificationCounter(amount = 1) }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 10.dp)
                .background(Color.LightGray)
        )
        OptionItem(
            R.drawable.ic_archive,
            "Хочу прочитать",
            additional = viewModel.wantToRead.toString(),
            onClick = { onNavigateToWantToRead() }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 10.dp)
                .background(Color.LightGray)
        )
        OptionItem(
            icon = R.drawable.ic_book_saved,
            text = "Понравиолось",
            additional = viewModel.liked.toString()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 10.dp)
                .background(Color.LightGray)
        )
        OptionItem(
            icon = R.drawable.ic_books,
            text = "Прочитано",
            additional = viewModel.readAmount.toString()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 10.dp)
                .background(Color.LightGray)
        )
        OptionItem(
            icon = R.drawable.ic_notification,
            text = "Уведомления",
            onClick = { onNavigateToOrdersNotifications() },
            notification = { NotificationCounter(amount = 3) })
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun AdminOptions(
    modifier: Modifier,
    onNavigateToAbout: () -> Unit,
    onNavigateToSupport: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DarkBgColor)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        OptionItem(
            icon = R.drawable.ic_help,
            text = "Поддержка",
            onClick = { onNavigateToSupport() })
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 10.dp)
                .background(Color.LightGray)
        )
        OptionItem(
            icon = R.drawable.ic_info,
            text = "О приложении",
            onClick = { onNavigateToAbout() }
        )
        Spacer(modifier = Modifier.height(10.dp))
    }

}

@Composable
fun OptionItem(
    icon: Int,
    text: String,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { onClick() }
    ) {
        Image(painter = painterResource(id = icon), contentDescription = "Leading Icon")
        Spacer(modifier = Modifier.width(10.dp))
        TextRobotoRegular(
            text = text,
            color = Color.Black,
            fontSize = 14,
        )
        Spacer(modifier = Modifier.width(10.dp))
    }
}

@Composable
fun OptionItem(
    icon: Int,
    text: String,
    onClick: () -> Unit = {},
    additional: String = ""
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { onClick() }
    ) {
        Image(painter = painterResource(id = icon), contentDescription = "Leading Icon")
        Spacer(modifier = Modifier.width(10.dp))
        TextRobotoRegular(
            text = text,
            color = Color.Black,
            fontSize = 14,
        )
        Spacer(modifier = Modifier.width(10.dp))
        TextRobotoRegular(
            text = additional,
            color = Color.Black,
            fontSize = 14,
        )
    }
}

@Composable
fun OptionItem(
    icon: Int,
    text: String,
    onClick: () -> Unit = {},
    notification: @Composable() () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { onClick() }
    ) {
        Image(painter = painterResource(id = icon), contentDescription = "Leading Icon")
        Spacer(modifier = Modifier.width(10.dp))
        TextRobotoRegular(
            text = text,
            color = Color.Black,
            fontSize = 14,
        )
        Spacer(modifier = Modifier.width(10.dp))
        notification()
    }
}

@Composable
private fun StatsHighlights(
    onNavigateToStatistics: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.last_read),
            contentDescription = "last read",
            modifier = Modifier
                .size(118.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) { onNavigateToStatistics() }
        )
        Image(
            painter = painterResource(id = R.drawable.fav_authors),
            contentDescription = "authors",
            modifier = Modifier
                .size(118.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) { onNavigateToStatistics() }
        )
        Image(
            painter = painterResource(id = R.drawable.fav_genres),
            contentDescription = "genres",
            modifier = Modifier
                .size(118.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) { onNavigateToStatistics() }
        )
    }
}

@Composable
fun NotificationCounter(amount: Int) {
    Box(
        modifier = Modifier
            .size(MaterialTheme.dimens.countBasketItemsSize.dp)
            .clip(CircleShape)
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = amount.toString(),
            color = Color.White,
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun ScreenHeader(
    text: String,
    goBack: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "Arrow Back",
            modifier = Modifier
                .size(38.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) { goBack() }
        )
        Spacer(modifier = Modifier.width(10.dp))
        TextRobotoRegular(
            text = text,
            color = Color.Black,
            fontSize = 18,
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(DarkBgColor)
    )
}

@Composable
fun UserNotAuthenticated(
    onNavigateToAbout: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToSignIn:() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBgColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.sign_in_smile),
                    contentDescription = "smile",
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }
            TextRobotoRegular(
                text = "Войдите в свой аккаунт, чтобы видеть заказы и сохранённые товары",
                color = Color.Black,
                fontSize = 14
            )
            CustomButton(
                text = "Войти или зарегистрироваться",
                textColor = Color.Black,
                modifier = Modifier.padding(vertical = 20.dp),
                onCLick = { onNavigateToSignIn() }
            )
            AdminOptions(
                modifier = Modifier,
                onNavigateToAbout = { onNavigateToAbout() },
                onNavigateToSupport = { onNavigateToSupport() }
            )
        }
    }

}


val DarkBgColor = Color(239, 235, 222)
val BorderColor = Color(217, 217, 217)
val DarkGreyColor = Color(140, 140, 140)
val linkColor = Color(14, 125, 255)
val logOutColor = Color(252, 87, 59)


