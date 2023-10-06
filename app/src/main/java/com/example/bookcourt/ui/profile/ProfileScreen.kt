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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.ui.theme.MainBgColor
import com.example.bookcourt.ui.theme.Roboto
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
    viewModel: ProfileViewModel = hiltViewModel()
) {
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
            onNavigateToAbout = { onNavigateToAbout() },
            onNavigateToSupport = { onNavigateToSupport() },
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

////val DarkBgColor = Color(239, 235, 222)
//val BorderColor = Color(217, 217, 217)
//val DarkGreyColor = Color(140, 140, 140)
//val linkColor = Color(14, 125, 255)
//val logOutColor = Color(252, 87, 59)
//val DarkBgColor = Color(0xFFEFEBDE)

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
            notification = { NotificationCounter(amount = 12) }
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
    onNavigateToAbout: () -> Unit,
    onNavigateToSupport: () -> Unit,
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
            .clickable(interactionSource =  MutableInteractionSource(),
                indication = null) { onClick() }
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
            .clickable(interactionSource =  MutableInteractionSource(),
                indication = null) { onClick() }
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
            .clickable(interactionSource =  MutableInteractionSource(),
                indication = null) { onClick() }
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
                .clickable(interactionSource =  MutableInteractionSource(),
                    indication = null) { onNavigateToStatistics() }
        )
        Image(
            painter = painterResource(id = R.drawable.fav_authors),
            contentDescription = "authors",
            modifier = Modifier
                .size(118.dp)
                .clickable(interactionSource =  MutableInteractionSource(),
                    indication = null) { onNavigateToStatistics() }
        )
        Image(
            painter = painterResource(id = R.drawable.fav_genres),
            contentDescription = "genres",
            modifier = Modifier
                .size(118.dp)
                .clickable(interactionSource =  MutableInteractionSource(),
                    indication = null) { onNavigateToStatistics() }
        )
    }
}

@Composable
fun NotificationCounter(amount: Int) {
    val width = when (amount) {
        in 0..0 -> 0.dp
        in 1..9 -> 16.dp
        in 11..99 -> 21.dp
        in 100..999 -> 26.dp
        else -> {
            31.dp
        }
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(16.dp)
            .width(width)
            .clip(RoundedCornerShape(50.dp))
            .background(Color.Red)
    ) {
        TextRobotoRegular(text = amount.toString(), color = Color.White, fontSize = 12)
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
                .clickable(interactionSource =  MutableInteractionSource(),
                    indication = null) { goBack() }
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




val DarkBgColor = Color(239, 235, 222)
val BorderColor = Color(217, 217, 217)
val DarkGreyColor = Color(140, 140, 140)
val linkColor = Color(14, 125, 255)
val logOutColor = Color(252, 87, 59)


