package com.example.bookcourt.ui.graphs

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookcourt.models.book.BookInfo
import com.example.bookcourt.ui.RecomendationScreen
import com.example.bookcourt.ui.auth.SignInScreen
import com.example.bookcourt.ui.statistics.Statistics
import com.example.bookcourt.ui.tutorial.TutorScreen
import com.example.bookcourt.utils.BottomBarScreen
import com.example.bookcourt.utils.Screens
import com.example.bookcourt.utils.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController, startDestination = Screens.Splash.route) {
        composable(BottomBarScreen.Library.route) {
//            LibraryScreen()
        }
//        composable(BottomBarScreen.Profile.route) {
//            ProfileScreen()
//        }
        composable(Screens.Recommendation.route) {
            BackHandler(true) {}
            RecomendationScreen(
                onNavigateToStatistics = { navController.navigate(Screens.Statistics.route) }
            )
        }
        composable(BottomBarScreen.ReadBook.route) {
//            ReadBookScreen()
        }
        composable(BottomBarScreen.AddBook.route) {
//           AddBookScreen()
        }
        composable(Screens.Tutorial.route) {
            TutorScreen(navController = navController)
//            CartScreen()
        }
        composable(Screens.SignIn.route) {
            SignInScreen(navController = navController)
//            com.example.bookcourt.ui.theme.Statistics()
//            TutorScreen()
        }
        composable(Screens.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(
            Screens.CardInfo.route +
                    "/{title}/{authorName}/{description}" +
                    "/{genre}" +
                    "/{numberOfPage}/{rate}/{price}",
            arguments = listOf(
                navArgument("title") {
                    type = NavType.StringType
                },
                navArgument("authorName") {
                    type = NavType.StringType
                },
                navArgument("description") {
                    type = NavType.StringType
                },
                navArgument("genre") {
                    type = NavType.StringType
                },
                navArgument("numberOfPage") {
                    type = NavType.StringType
                },
                navArgument("rate") {
                    type = NavType.IntType
                },
                navArgument("price") {
                    type = NavType.IntType
                },
//            navArgument("buyUri"){
//                type = NavType.StringType
//            }
            )
        ) {
            val title = it.arguments?.getString("title") ?: "unset"
            val authorName = it.arguments?.getString("authorName") ?: "unset"
            val description = it.arguments?.getString("description") ?: "unset"
            val genre = it.arguments?.getString("genre") ?: "unset"
            val numberOfPage = it.arguments?.getString("numberOfPage") ?: "unset"
            val rate = it.arguments?.getInt("rate") ?: 0
            val price = it.arguments?.getInt("price") ?: 0
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
        composable(Screens.Statistics.route) {
            BackHandler(true) {

            }
            Statistics(navController = navController)

//            SwipableStats(navController = navController)
        }
    }
}