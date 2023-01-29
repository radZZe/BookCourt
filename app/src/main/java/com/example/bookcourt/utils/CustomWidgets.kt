package com.example.bookcourt.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookcourt.R

//@Preview
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
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_swipe_up),
                    contentDescription = "",
                    tint = colorResource(R.color.main_color)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Добавить в желаемое",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_swipe_right),
                    contentDescription = "",
                    tint = colorResource(R.color.main_color)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Нравится",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_swipe_left),
                    contentDescription = "",
                    tint = colorResource(R.color.main_color)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Не нравится",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_swipe_down),
                    contentDescription = "",
                    tint = colorResource(R.color.main_color)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Пропустить",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onCLick()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.main_color)
                    )
                ) {
                    Text(
                        text = "OK!",
                        color = Color.White,
                        fontSize = 22.sp,
                    )
                }
            }
        }
    }
}
