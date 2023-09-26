package com.example.bookcourt.ui.basket.successPurchase

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.bookcourt.R
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.order.Order
import com.example.bookcourt.ui.theme.Roboto
import com.example.bookcourt.ui.theme.GrayText
import com.example.bookcourt.ui.theme.GreenText
import com.example.bookcourt.ui.theme.OrangeStatusColor

@Composable
fun OrderWidget(
    order: Order
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text( text = order.title,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 16.sp
        )
        OrderStatus(status = order.status)
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            order.books.forEach {   book->
                OrderedBook(book = book)
            }
        }
    }
}

@Composable
private fun OrderStatus(status:String){
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Canvas(modifier = Modifier
            .size(10.dp)
            .align(Alignment.CenterVertically),
            onDraw = {
            drawCircle(
                color = if (status!="Доставлен"){ //TODO("заменить хардкод, когда будет понятно по апи")
                    OrangeStatusColor
                } else{
                    GreenText
                }
            )
        })
        Text( text = status,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun OrderedBook(book: Book){
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SubcomposeAsyncImage(
            model = book.bookInfo.image,
            contentDescription = "${book.bookInfo.author}'s book image",
            modifier = Modifier
                .width(65.dp)
                .height(89.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.FillBounds,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .scale(0.5f)
                )
            }
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text( text = book.bookInfo.title,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                fontSize = 16.sp
            )
            Text( text = book.bookInfo.genre,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                color = GrayText,
                fontSize = 16.sp
            )
            Text( text = stringResource(id = R.string.book_price,book.bookInfo.price),
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}