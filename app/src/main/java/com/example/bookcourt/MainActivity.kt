package com.example.bookcourt

import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.*
import coil.request.ImageRequest
import coil.size.Size.Companion.ORIGINAL
import com.example.bookcourt.models.Book
import com.example.bookcourt.ui.theme.BookCourtTheme
import com.example.bookcourt.utils.CardStack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookCourtTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    MainContent()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainContent() {
    Column(Modifier.padding(20.dp)) {
        val isEmpty = remember {
            mutableStateOf(false)
        }
        Text(
            text = "Рекомендации",
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 5.dp),
            style = MaterialTheme.typography.body1
        )
//        BookCard()
        CardStack(items = books,onEmptyStack = {
            isEmpty.value = true
        })
    }
}

//@Composable
//fun BookCard() {
//    Card(
//        elevation = 10.dp,
//        shape = RoundedCornerShape(20.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .fillMaxHeight(0.8f)
//    ) {
//        Box(){
//            Column(
//                modifier = Modifier.fillMaxSize()
//            ) {
//                BookCardImage(uri = "https://cv6.litres.ru/pub/c/elektronnaya-kniga/cover_415/36628165-ray-dalio-principy-zhizn-i-rabota.webp")
//                Text(text = "Book.name", fontWeight = FontWeight.Bold)
//                Row() {
//                    Text(text = "Book.author", color = Color.Black)
//                    Text(text = "Book.createdAt")
//                    Text(text = "Book.numberOfPage")
//                }
//                Text(text = "Book.description")
//            }
//
//        }
//    }
//}

@Composable
fun BookCardImage(uri: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f), contentAlignment = Alignment.Center
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .size(coil.size.Size.ORIGINAL) // Set the target size to load the image at.
                .build(),
        )

        if (painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator()
            Log.d("Image", "Loading...")
        }

        Image(
            painter = painter,
            contentDescription = "Book Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun DefaultPreview() {
//    BookCourtTheme {
//        BookCard()
//    }
//}

val books = mutableListOf<Book>(
    Book(
        "Test name",
        "Test author",
        "FDSFDSFDSFF",
        "1932",
        "159",
        5,
        "Danila Razdobarov",
        "War","https://cv6.litres.ru/pub/c/elektronnaya-kniga/cover_415/36628165-ray-dalio-principy-zhizn-i-rabota.webp"
    ),
    Book(
        "Test name2",
        "Test author",
        "FDSFDSFDSFF",
        "1932",
        "159",
        5,
        "Danila Razdobarov",
        "War","https://cv6.litres.ru/pub/c/elektronnaya-kniga/cover_415/36628165-ray-dalio-principy-zhizn-i-rabota.webp"
    ),
    Book(
        "Test name3",
        "Test author",
        "FDSFDSFDSFF",
        "1932",
        "159",
        5,
        "Danila Razdobarov",
        "War","https://cv6.litres.ru/pub/c/elektronnaya-kniga/cover_415/36628165-ray-dalio-principy-zhizn-i-rabota.webp"
    )

)