package com.example.bookcourt.ui.graphs

import com.example.bookcourt.ui.statistics.FavoriteAuthorsStats
import com.example.bookcourt.ui.statistics.FavoriteGenresStats
import com.example.bookcourt.ui.statistics.ReadBooksStats
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bookcourt.models.BookInfo
import com.example.bookcourt.ui.CardInfoScreen
//import com.example.bookcourt.ui.ProfileScreen
import com.example.bookcourt.ui.RecomendationScreen
import com.example.bookcourt.ui.auth.SignInScreen
import com.example.bookcourt.ui.auth.SignInViewModel
import com.example.bookcourt.ui.statistics.StatisticsViewModel
import com.example.bookcourt.utils.BottomBarScreen
import com.example.bookcourt.utils.Screens
import com.example.bookcourt.utils.SplashScreen
import com.example.bookcourt.utils.SplashViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    splashScreenViewModel: SplashViewModel,
    signInScreenViewModel: SignInViewModel,
    statisticsViewModel: StatisticsViewModel
) {
    NavHost(navController, startDestination = Screens.Splash.route) {
        composable(BottomBarScreen.Library.route) {
//            LibraryScreen()
        }
//        composable(BottomBarScreen.Profile.route) {
//            ProfileScreen()
//        }
        composable(BottomBarScreen.Recomendations.route) {
            RecomendationScreen(navController)
        }
        composable(BottomBarScreen.ReadBook.route) {
//            ReadBookScreen()
        }
        composable(BottomBarScreen.AddBook.route) {
//           AddBookScreen()
        }
        composable(BottomBarScreen.Cart.route) {
//            CartScreen()
        }
        composable(Screens.SignIn.route) {
            SignInScreen(navController = navController, mViewModel = signInScreenViewModel)
        }
        composable(Screens.Splash.route) {
            SplashScreen(navController = navController, mViewModel = splashScreenViewModel)
        }
        composable(Screens.CardInfo.route+
                "/{title}/{authorName}/{description}" +
                "/{genre}" +
                "/{numberOfPage}/{rate}/{price}",
        arguments = listOf(
            navArgument("title"){
                type = NavType.StringType
            },
            navArgument("authorName"){
                type = NavType.StringType
            },
            navArgument("description"){
                type = NavType.StringType
            },
            navArgument("genre"){
                type = NavType.StringType
            },
            navArgument("numberOfPage"){
                type = NavType.StringType
            },
            navArgument("rate"){
                type = NavType.IntType
            },
            navArgument("price"){
                type = NavType.IntType
            },
//            navArgument("buyUri"){
//                type = NavType.StringType
//            }
            )
        ) {
            val title  = it.arguments?.getString("title")?:"unset"
            val authorName = it.arguments?.getString("authorName")?:"unset"
            val description = it.arguments?.getString("description")?:"unset"
            val genre = it.arguments?.getString("genre")?:"unset"
            val numberOfPage = it.arguments?.getString("numberOfPage")?:"unset"
            val rate = it.arguments?.getInt("rate")?:0
            val price = it.arguments?.getInt("price")?:0
            val book = BookInfo(
//                bookId = "0",
                title = title,
                author = authorName,
                description,
                numberOfPage,
                rate,
                genre,
                price,
                image = ""
            )
//            CardInfoScreen(navController,book)
        }

        composable(Screens.StatisticsRead.route) {
            ReadBooksStats(navController = navController, statisticsViewModel)
        }
        composable(Screens.StatisticsFavGenres.route) {
            FavoriteGenresStats(navController = navController, statisticsViewModel)
        }
        composable(Screens.StatisticsFavAuthors.route) {
            FavoriteAuthorsStats(navController = navController, statisticsViewModel)
        }
    }
}