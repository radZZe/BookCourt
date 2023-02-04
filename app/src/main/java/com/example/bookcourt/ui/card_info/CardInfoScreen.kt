package com.example.bookcourt.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bookcourt.R
import com.example.bookcourt.models.Book
import com.example.bookcourt.ui.theme.Brown
import com.example.bookcourt.utils.BottomBarScreen
import com.example.bookcourt.utils.Screens

@Composable
fun CardInfoScreen(navController: NavController, book: Book) {
    BookInfoCard(book,navController)
}

@Composable
fun BookInfoCard(book: Book,navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Box {
            Column(
                modifier = Modifier.padding(top = 28.dp, start = 15.dp, end = 15.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = book.name,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(
                            Font(
                                R.font.manrope_extrabold,
                                weight = FontWeight.W600
                            )
                        ),
                    )
                    Icon(painter = painterResource(id = R.drawable.close_square),
                        contentDescription = "",
                        tint = Brown,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable {
                                navController.popBackStack()
                            })
                }
                Text(
                    text = book.author, fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.manrope_medium, weight = FontWeight.W600)),
                    color = Color(0xFF636363)
                )
            }
            CardStat(book)
            Text(
                text = book.genre, color = Color(0xFF2C1E83),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.manrope_semibold, weight = FontWeight.W500)),
                modifier = Modifier
                    .padding(top = 84.dp, start = 15.dp)
            )
            Text(
                text = book.description,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.manrope_semibold, weight = FontWeight.W400)),
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(top = 220.dp, end = 15.dp, start = 15.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun CardStat(book: Book) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 123.dp, start = 15.dp, end = 15.dp)
            .width(width = 62.dp)
            .size(size = 62.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color(0xFFF9F9F9))//0xFFF9F9F9

    ) {
        Row(
            Modifier
                .padding(end = 23.dp, start = 10.dp)
                .fillMaxWidth(),

            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier)
            Column(
                modifier = Modifier.padding(top = 7.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Лайки", fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.manrope_semibold,
                            weight = FontWeight.W600
                        )
                    ),
                    color = Color(0xFF636363),
                    modifier = Modifier
                )
                Text(
                    text = "423", fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.manrope_semibold,
                            weight = FontWeight.W600
                        )
                    ),
                    color = Color(0xFF000000),
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(top = 6.dp, bottom = 6.dp)
                    .background(color = Color(0xFFD9D9D9))
            ) {
            }

            Box(modifier = Modifier)
            Column(
                modifier = Modifier.padding(top = 7.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Оценка", fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.manrope_semibold,
                            weight = FontWeight.W600
                        )
                    ),
                    color = Color(0xFF636363),
                    modifier = Modifier
                )
                Text(
                    text = book.rate.toString(), fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.manrope_semibold,
                            weight = FontWeight.W600
                        )
                    ),
                    color = Color(0xFF000000),
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(top = 6.dp, bottom = 6.dp)
                    .background(color = Color(0xFFD9D9D9))
            ) {
            }
            Box(modifier = Modifier)
            Column(
                modifier = Modifier.padding(top = 7.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Страниц", fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.manrope_semibold,
                            weight = FontWeight.W600
                        )
                    ),
                    color = Color(0xFF636363),
                    modifier = Modifier
                )
                Text(
                    text = book.numberOfPage, fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.manrope_semibold,
                            weight = FontWeight.W600
                        )
                    ),
                    color = Color(0xFF000000),
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(top = 6.dp, bottom = 6.dp)
                    .background(color = Color(0xFFD9D9D9))

            ) {
            }
            Box(modifier = Modifier)
            Column(
                modifier = Modifier.padding(top = 7.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Цена", fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.manrope_semibold,
                            weight = FontWeight.W600
                        )
                    ),
                    color = Color(0xFF636363),
                    modifier = Modifier
                )
                Text(
                    text = "679₽", fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.manrope_semibold,
                            weight = FontWeight.W600
                        )
                    ),
                    color = Color(0xFF000000),
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
        }
    }
}
