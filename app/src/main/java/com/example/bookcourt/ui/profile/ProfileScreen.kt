package com.example.bookcourt.ui

import android.content.Context
import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.models.Statistics
import com.example.bookcourt.models.User
import com.example.bookcourt.ui.profile.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val user = viewModel.user
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        val userId =
            "b9786ae1-4efb-46c4-a493-cb948cb80103" // Так как нет авторизации я храню просто id пользователя в тестовом формате
        viewModel.getUserData(userId = userId)
    }


    Scaffold() {
        Box{
            AnimatedVisibility(
                visible = viewModel.alertDialogState.value,
                modifier = Modifier.zIndex(2f),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.35f)
                        .background(Color.Black).clickable {
                            viewModel.dismiss()
                        }
                )
            }
            AnimatedVisibility(
                modifier = Modifier
                    .zIndex(3f)
                    .align(Alignment.Center),
                visible = viewModel.feedbackState.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FeedBackCard(viewModel)
            }
            AnimatedVisibility(
                modifier = Modifier
                    .zIndex(3f)
                    .align(Alignment.Center),
                visible = viewModel.statisticsState.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                StatisticsCard(viewModel,user.value!!,context)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f)
            ) {
                if (user.value != null) {
                    HeaderProfile(user.value!!, viewModel)
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                            .background(Color.Black)
                            .fillMaxWidth()
                            .height(2.dp)
                    )
                    ProfileMenu(viewModel = viewModel)
                    //StatisticsComponent(user.value!!.statistics)
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

            }
        }

    }


}

@Composable
fun HeaderProfile(user: User, viewModel: ProfileViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 20.dp, start = 20.dp)
    ) {
        UserPhotoComponent(user.image)
        Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
            Text(text = "${user.name} ${user.surname}")
            Text(text = user.email)
        }

    }

}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}


@Composable
fun UserPhotoComponent(uri: String) {
    AsyncImage(
        model = uri,
        contentDescription = "Book Cover",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
    )
}

@Composable
fun StatisticsCard(viewModel:ProfileViewModel,user: User,context:Context) {
    Box(
        modifier = Modifier
            .padding(30.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White),
        contentAlignment = Center
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Ваша Статистика",
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h5)
            StatisticsComponent(user.statistics)

            Button(onClick = {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT, "Моя статистика в приложении BookСourt:" + "\n"

                                + "Количество прочитанных мною книг: ${user.statistics.numberOfReadBooks}"
                                + "\n"
                                + "Количество книг которые мне понравились:${user.statistics.numberOfLikedBooks}"
                                + "\n" + "Мои любимые жанры: ${user.statistics.favoriteGenreList[0]} , " +
                                "${user.statistics.favoriteGenreList[1]} , " +
                                "${user.statistics.favoriteGenreList[2]} "
                    )
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            }) {
                Text(text = "Поделиться")
            }
        }

    }
}

@Composable
fun StatisticsComponent(statistics: Statistics) {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(text = "Количество прочитанных книг: ${statistics.numberOfReadBooks}")
        Text(text = "Количество книг которые вам понравились:${statistics.numberOfLikedBooks}")
        Text(text = "Топ 3 любимых жанра:")
        statistics.favoriteGenreList.forEachIndexed { index, it ->
            Text(text = "${index + 1}. ${it}")
        }
    }
}



@Composable
fun FeedBackCard(viewModel: ProfileViewModel){
    Box(
        modifier = Modifier
            .padding(30.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            ,
        contentAlignment = Center
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Обратная связь",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(20.dp))
            TextField(modifier = Modifier.fillMaxWidth(),value = viewModel.feedbackData.value, onValueChange = { string ->
                viewModel.feedbackDataChanged(string)
            },
                placeholder = {
                    Text(text = "Введите номер телефона или электронную почту")
                })
            Spacer(modifier = Modifier
                .height(10.dp)
                .fillMaxWidth())

            TextField(modifier = Modifier.fillMaxWidth(),value = viewModel.feedbackMessage.value, onValueChange = {
                viewModel.feedbackMessageChanged(it)
            },
                placeholder = {
                    Text(text = "Введите свое сообщение")
                })
            Spacer(modifier = Modifier
                .height(10.dp)
                .fillMaxWidth())

            Button(onClick = {
                viewModel.feedbackStateChanged()
            }) {
                Text(text = "Отправить")
            }
        }

    }
}

@Composable
fun ProfileMenu(viewModel: ProfileViewModel){
    Column(modifier = Modifier
        .fillMaxHeight(0.5f)
        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, ) {
        ProfileMenuItem("Настройки"){}
        ProfileMenuItem(text = "Статистика") {
            viewModel.statisticsStateChanged()
        }
        ProfileMenuItem(text = "Обратная связь") {
            viewModel.feedbackStateChanged()
        }
    }
}
@Composable
fun ProfileMenuItem(text:String,onClick:()->Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clip(RoundedCornerShape(15.dp))
        .background(
            Color.White
        )
        .border(1.dp, Color.Black, RoundedCornerShape(15.dp)),
    contentAlignment = Center){
        Text(text = text,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h5, modifier = Modifier
                .clickable {
                    onClick()
                }
                .padding(10.dp))
    }
}

