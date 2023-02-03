package com.example.bookcourt.ui.graphs

import StatisticsScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookcourt.ui.ProfileScreen
import com.example.bookcourt.ui.RecomendationScreen
import com.example.bookcourt.ui.auth.SignInScreen
import com.example.bookcourt.ui.auth.SignInViewModel
import com.example.bookcourt.ui.statistics.StatisticsViewModel
import com.example.bookcourt.utils.BottomBarScreen
import com.example.bookcourt.utils.Screens
import com.example.bookcourt.utils.SplashScreen
import com.example.bookcourt.utils.SplashViewModel

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
        composable(BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
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
        composable(Screens.Statistics.route) {
            StatisticsScreen(navController = navController, statisticsViewModel)
        }

    }
}