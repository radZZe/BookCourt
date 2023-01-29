package com.example.bookcourt.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.*
import coil.request.ImageRequest
import coil.size.Size.Companion.ORIGINAL
import com.example.bookcourt.models.Book
import com.example.bookcourt.ui.theme.BookCourtTheme
import com.example.bookcourt.utils.BottomBarScreen
import com.example.bookcourt.utils.CardStack
import com.example.bookcourt.utils.rememberCardStackController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookcourt.ui.graphs.NavigationGraph
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookCourtTheme {
                setDataJson(LocalContext.current)
               val  navController: NavHostController = rememberNavController()
                Scaffold(
                    bottomBar = { com.example.bookcourt.utils.BottomNavigation(navController = navController) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        NavigationGraph(navController)
                    }
                }

            }
        }
    }
}

fun setDataJson(context: Context){
    if(!File(context.filesDir,"book.json").exists()){
        File(context.filesDir,"book.json").writeText("[\n" +
                "  {\n" +
                "    \"data\": {\n" +
                "      \"author\": \"Test author\",\n" +
                "      \"createdAt\": \"1992\",\n" +
                "      \"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip\",\n" +
                "      \"genre\": \"Комедия\",\n" +
                "      \"image\": \"https://cv6.litres.ru/pub/c/elektronnaya-kniga/cover_415/36628165-ray-dalio-principy-zhizn-i-rabota.webp\",\n" +
                "      \"loadingAt\": \"1672919896\",\n" +
                "      \"name\": \"Test name\",\n" +
                "      \"numberOfPage\": \"143\",\n" +
                "      \"owner\": \"b9786ae1-4efb-46c4-a493-cb948cb80103\",\n" +
                "      \"rate\": 5\n" +
                "    },\n" +
                "    \"id\": \"94001f0-e0f1-4166-b06f-818956c145bf\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"data\": {\n" +
                "      \"author\": \"Test author1\",\n" +
                "      \"createdAt\": \"1392\",\n" +
                "      \"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip\",\n" +
                "      \"genre\": \"Комедия\",\n" +
                "      \"image\": \"https://cv6.litres.ru/pub/c/elektronnaya-kniga/cover_415/36628165-ray-dalio-principy-zhizn-i-rabota.webp\",\n" +
                "      \"loadingAt\": \"1672919896\",\n" +
                "      \"name\": \"Test name1\",\n" +
                "      \"numberOfPage\": \"123\",\n" +
                "      \"owner\": \"b9786ae1-4efb-46c4-a493-cb948cb80103\",\n" +
                "      \"rate\": 5\n" +
                "    },\n" +
                "    \"id\": \"890b9b88-4bf9-46f3-a4df-9c584c188423\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"data\": {\n" +
                "      \"author\": \"Test author2\",\n" +
                "      \"createdAt\": \"1322\",\n" +
                "      \"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip\",\n" +
                "      \"genre\": \"Комедия\",\n" +
                "      \"image\": \"https://cv6.litres.ru/pub/c/elektronnaya-kniga/cover_415/36628165-ray-dalio-principy-zhizn-i-rabota.webp\",\n" +
                "      \"loadingAt\": \"1672919896\",\n" +
                "      \"name\": \"Test name2\",\n" +
                "      \"numberOfPage\": \"143\",\n" +
                "      \"owner\": \"b9786ae1-4efb-46c4-a493-cb948cb80103\",\n" +
                "      \"rate\": 5\n" +
                "    },\n" +
                "    \"id\": \"e8302cd0-c6c7-4f03-b896-005e6728eaee\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"data\": {\n" +
                "      \"author\": \"Test author3\",\n" +
                "      \"createdAt\": \"1211\",\n" +
                "      \"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip\",\n" +
                "      \"genre\": \"Комедия\",\n" +
                "      \"image\": \"https://cv6.litres.ru/pub/c/elektronnaya-kniga/cover_415/36628165-ray-dalio-principy-zhizn-i-rabota.webp\",\n" +
                "      \"loadingAt\": \"1672919896\",\n" +
                "      \"name\": \"Test name3\",\n" +
                "      \"numberOfPage\": \"122\",\n" +
                "      \"owner\": \"b9786ae1-4efb-46c4-a493-cb948cb80103\",\n" +
                "      \"rate\": 5\n" +
                "    },\n" +
                "    \"id\": \"f94001f0-e0f1-4166-b06f-818956c145bf\"\n" +
                "  }\n" +
                "]")
    }
    if(!File(context.filesDir,"user.json").exists()){
        File(context.filesDir,"user.json").writeText("{\n" +
                "    \"b9786ae1-4efb-46c4-a493-cb948cb80103\": {\n" +
                "        \"createdAt\": \"1672919896\",\n" +
                "        \"email\": \"example@mail.ru\",\n" +
                "        \"image\": \"https://telesputnik.ru/img/no-avatar-comment.png\",\n" +
                "        \"name\": \"Danila\",\n" +
                "        \"password\": \"1f6e25cce3295e79d4030bdacd86010d0f5588ac20874790032f411d52297c25\",\n" +
                "        \"statistics\": {\n" +
                "            \"favoriteGenreList\": [\n" +
                "                \"Комедия\",\n" +
                "                \"Драма\",\n" +
                "                \"Трагедия\"\n" +
                "            ],\n" +
                "            \"numberOfLikedBooks\": \"5\",\n" +
                "            \"numberOfReadBooks\": \"3\"\n" +
                "        },\n" +
                "        \"surname\": \"Razdobarov\",\n" +
                "        \"token\": \"b9786ae1-4efb-46c4-a493-cb948cb80103\"\n" +
                "    },\n" +
                "    \"dc08b22c-f112-4bc1-ad68-3251561b1d49\": {\n" +
                "        \"createdAt\": \"1672919896\",\n" +
                "        \"email\": \"example1@mail.ru\",\n" +
                "        \"image\": \"https://cdn.icon-icons.com/icons2/2506/PNG/512/user_icon_150670.png\",\n" +
                "        \"name\": \"Anton\",\n" +
                "        \"password\": \"1f6e25cce3295e79d4030bdacd86010d0f5588ac20874790032f411d52297c25\",\n" +
                "        \"statistics\": {\n" +
                "            \"favoriteGenreList\": [\n" +
                "                \"Комедия\",\n" +
                "                \"Драма\",\n" +
                "                \"Трагедия\"\n" +
                "            ],\n" +
                "            \"numberOfLikedBooks\": \"5\",\n" +
                "            \"numberOfReadBooks\": \"3\"\n" +
                "        },\n" +
                "        \"surname\": \"Davydov\",\n" +
                "        \"token\": \"b9786ae1-4efb-46c4-a493-cb948cb80103\"\n" +
                "    }\n" +
                "}")
    }
//    var json = File(context.filesDir,"book.json").readText()
}


