//package com.example.bookcourt.ui
//
//import android.content.Context
//import android.content.Intent
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Alignment.Companion.Center
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.zIndex
//import androidx.hilt.navigation.compose.hiltViewModel
//import coil.compose.AsyncImage
//import com.example.bookcourt.R
//import com.example.bookcourt.models.user.UserStatistics
//import com.example.bookcourt.models.user.User
//import com.example.bookcourt.ui.profile.ProfileViewModel
//
//@Composable
//fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
//    val user = viewModel.user
//    val context = LocalContext.current
//
//    LaunchedEffect(key1 = Unit) {
//        val userId =
//            "b9786ae1-4efb-46c4-a493-cb948cb80103" // Так как нет авторизации я храню просто id пользователя в тестовом формате
//        viewModel.getUserData(context)
//    }
//
//    Scaffold() {
//        Box {
//            AnimatedVisibility(
//                visible = viewModel.alertDialogState.value,
//                modifier = Modifier.zIndex(2f),
//                enter = fadeIn(),
//                exit = fadeOut()
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .alpha(0.35f)
//                        .background(Color.Black)
//                        .clickable {
//                            viewModel.dismiss()
//                        }
//                )
//            }
//            AnimatedVisibility(
//                modifier = Modifier
//                    .zIndex(3f)
//                    .align(Alignment.Center),
//                visible = viewModel.feedbackState.value,
//                enter = fadeIn(),
//                exit = fadeOut()
//            ) {
//                FeedBackCard(viewModel)
//            }
//            AnimatedVisibility(
//                modifier = Modifier
//                    .zIndex(3f)
//                    .align(Alignment.Center),
//                visible = viewModel.statisticsState.value,
//                enter = fadeIn(),
//                exit = fadeOut()
//            ) {
//                StatisticsCard(user.value!!, context)
//            }
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .zIndex(1f)
//            ) {
//                if (user.value != null) {
//                    HeaderProfile(user.value!!, viewModel)
//                    Box(
//                        modifier = Modifier
//                            .padding(10.dp)
//                            .background(Color.Black)
//                            .fillMaxWidth()
//                            .height(2.dp)
//                    )
//                    ProfileMenu(viewModel = viewModel)
//                    //StatisticsComponent(user.value!!.statistics)
//                } else {
//                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                        CircularProgressIndicator()
//                    }
//                }
//
//            }
//        }
//
//    }
//
//
//}
//
//@Composable
//fun HeaderProfile(user: User, viewModel: ProfileViewModel) {
//    val userName = viewModel.userName.collectAsState(initial = "")
//    val userSurname = viewModel.userSurname.collectAsState(initial = "")
//    val userPhone = viewModel.userPhone.collectAsState(initial = "")
//    val userCity = viewModel.userCity.collectAsState(initial = "")
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(10.dp), contentAlignment = Center
//    ) {
//        Surface(
//            shape = RoundedCornerShape(15.dp),
//            modifier = Modifier
//                .fillMaxWidth(1f)
//        ) {
//            Row(
//                modifier = Modifier
//                    .background(colorResource(id = R.color.main_color))
//                    .padding(10.dp)
//            ) {
////                UserPhotoComponent(user.image)
//                Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
//                    Text(
//                        text = "${userName.value} ${userSurname.value}",
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 18.sp
//                    )
//                    Row() {
//                        Text(
//                            text = userPhone.value,
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 18.sp
//                        )
//                        Spacer(modifier = Modifier.width(20.dp))
//                        Text(
//                            text = userCity.value,
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 18.sp
//                        )
//                    }
//                }
//
//            }
//        }
//    }
//
//
//}
//
//@Preview(showBackground = true)
//@Composable
//fun ProfileScreenPreview() {
//    ProfileScreen()
//}
//
//
//@Composable
//fun UserPhotoComponent(uri: String) {
//    AsyncImage(
//        model = uri,
//        contentDescription = stringResource(R.string.user_photo),
//        contentScale = ContentScale.Crop,
//        modifier = Modifier
//            .size(60.dp)
//            .clip(CircleShape)
//    )
//}
//
//@Composable
//fun StatisticsCard(user: User, context: Context) {
//    Box(
//        modifier = Modifier
//            .padding(30.dp)
//            .clip(RoundedCornerShape(15.dp))
//            .background(Color.White),
//        contentAlignment = Center
//    ) {
//        Column(
//            modifier = Modifier.padding(20.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = stringResource(R.string.your_statistics),
//                modifier = Modifier.padding(10.dp),
//                textAlign = TextAlign.Center,
//                fontWeight = FontWeight.Bold,
//                style = MaterialTheme.typography.h5
//            )
//            StatisticsComponent(user.statistics)
//
//            Button(onClick = {
//                val sendIntent: Intent = Intent().apply {
//                    action = Intent.ACTION_SEND
//                    putExtra(
//                        Intent.EXTRA_TEXT,
//                        context.getString(R.string.user_statistics_in_the_app) + "\n"
//
//                                + context.getString(R.string.number_of_books_read_by_the_user) + "${user.statistics.numberOfReadBooks}"
//                                + "\n"
//                                + context.getString(R.string.books_user_liked) + "${user.statistics.numberOfLikedBooks}"
//                                + "\n" + context.getString(R.string.users_favorite_genres) + " ${user.statistics.favoriteGenreList[0]} , " +
//                                "${user.statistics.favoriteGenreList[1]} , " +
//                                "${user.statistics.favoriteGenreList[2]} " +
//                                "\n${getWantedBooks(user.statistics.wantToRead)}"
//                    )
//                    type = "text/plain"
//                }
//                val shareIntent = Intent.createChooser(sendIntent, null)
//                context.startActivity(shareIntent)
//            }) {
//                Text(text = stringResource(R.string.share))
//            }
//        }
//
//    }
//}
//
//@Composable
//fun StatisticsComponent(statistics: Statistics) {
//    Column(modifier = Modifier.padding(10.dp)) {
//        Text(text = stringResource(R.string.numberOfReadBooks) + " ${statistics.numberOfReadBooks}")
//        Spacer(modifier = Modifier.height(10.dp))
//        Text(text = stringResource(R.string.numberOfLikedBooks) + "${statistics.numberOfLikedBooks}")
//        Spacer(modifier = Modifier.height(10.dp))
//        Text(text = stringResource(R.string.top_favorite_genres))
//        statistics.favoriteGenreList.forEachIndexed { index, it ->
//            Text(text = "${index + 1}. ${it}")
//        }
//        Spacer(modifier = Modifier.height(10.dp))
//        Text(text = "Хочу прочитать:")
//        statistics.wantToRead.forEachIndexed { index, book ->
//            Text(text = "${index + 1} $book")
//        }
////        Text(text = getWantedBooks(statistics.wantToRead))
//    }
//}
//
//
//@Composable
//fun FeedBackCard(viewModel: ProfileViewModel) {
//    Box(
//        modifier = Modifier
//            .padding(30.dp)
//            .clip(RoundedCornerShape(15.dp))
//            .background(Color.White),
//        contentAlignment = Center
//    ) {
//        Column(
//            modifier = Modifier.padding(20.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = stringResource(R.string.feedback),
//                textAlign = TextAlign.Center,
//                fontWeight = FontWeight.Bold,
//                style = MaterialTheme.typography.h5
//            )
//            Spacer(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(20.dp)
//            )
//            TextField(modifier = Modifier.fillMaxWidth(),
//                value = viewModel.feedbackData.value,
//                onValueChange = { string ->
//                    viewModel.feedbackDataChanged(string)
//                },
//                placeholder = {
//                    Text(text = stringResource(R.string.placeholder_feedbackData))
//                })
//            Spacer(
//                modifier = Modifier
//                    .height(10.dp)
//                    .fillMaxWidth()
//            )
//
//            TextField(modifier = Modifier.fillMaxWidth(),
//                value = viewModel.feedbackMessage.value,
//                onValueChange = {
//                    viewModel.feedbackMessageChanged(it)
//                },
//                placeholder = {
//                    Text(text = stringResource(R.string.placeholder_feedbackMessage))
//                })
//            Spacer(
//                modifier = Modifier
//                    .height(10.dp)
//                    .fillMaxWidth()
//            )
//
//            Button(onClick = {
//                viewModel.feedbackStateChanged()
//            }) {
//                Text(text = stringResource(R.string.send_btn))
//            }
//        }
//
//    }
//}
//
//@Composable
//fun ProfileMenu(viewModel: ProfileViewModel) {
//    Column(
//        modifier = Modifier
//            .fillMaxHeight(0.5f)
//            .fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        ProfileMenuItem(stringResource(R.string.settings)) {}
//        ProfileMenuItem(text = stringResource(R.string.statistics)) {
//            viewModel.statisticsStateChanged()
//        }
//        ProfileMenuItem(text = stringResource(R.string.feedback)) {
//            viewModel.feedbackStateChanged()
//        }
//    }
//}
//
//@Composable
//fun ProfileMenuItem(text: String, onClick: () -> Unit) {
//    Button(
//        shape = RoundedCornerShape(15.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(10.dp),
//        onClick = { onClick() },
//        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.main_color))
//    ) {
//        Row(
//            modifier = Modifier.background(colorResource(id = R.color.main_color)),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = text,
//                textAlign = TextAlign.Center,
//                fontWeight = FontWeight.Bold,
//                style = MaterialTheme.typography.h5, modifier = Modifier
//                    .padding(10.dp),
//                color = Color.White
//            )
//        }
//
//    }
//}
//
//fun getWantedBooks(list: List<String>): String {
//    var res = "Хочу прочитать: \n"
//    list.forEachIndexed { number, book ->
//        res += "${number + 1} $book \n"
//    }
//    return res
//}
//
