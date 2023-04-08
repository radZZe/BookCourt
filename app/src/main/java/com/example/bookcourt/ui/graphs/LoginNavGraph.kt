package com.example.bookcourt.ui.graphs

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.bookcourt.ui.auth.SignInScreen
import com.example.bookcourt.ui.tutorial.TutorScreen
import com.example.bookcourt.utils.Graph
import com.example.bookcourt.utils.Screens
import com.example.bookcourt.utils.SplashScreen

fun NavGraphBuilder.loginNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.LOGIN,
        startDestination = Screens.Splash.route
    ) {
        composable(Screens.Tutorial.route) {
            TutorScreen(navController = navController)
        }
        composable(Screens.SignIn.route) {
            SignInScreen(navController = navController)
        }
        composable(Screens.Splash.route) {
            SplashScreen(navController = navController)
        }
    }
}