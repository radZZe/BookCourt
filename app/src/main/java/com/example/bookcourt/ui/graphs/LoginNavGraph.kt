package com.example.bookcourt.ui.graphs

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.bookcourt.ui.auth.SignInScreen
import com.example.bookcourt.ui.categorySelection.CategorySelectionScreen
import com.example.bookcourt.ui.tutorial.TutorScreen
import com.example.bookcourt.ui.verification.VerificationCodeScreen
import com.example.bookcourt.utils.Graph
import com.example.bookcourt.utils.Screens
import com.example.bookcourt.utils.SplashScreen

fun NavGraphBuilder.loginNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.LOGIN,
        startDestination = Screens.Splash.route
    ) {
        composable(Screens.Tutorial.route) {

            TutorScreen(
                onNavigateToCategorySelection = {
                    navController.navigate(Screens.CategorySelection.route)
                }
            )
        }
        composable(Screens.SignIn.route) {

            SignInScreen(
                onNavigateToVerificationCode = {navController.navigate(Screens.VerificationCode.route)},
                onNavigateToCategorySelection = {navController.navigate(Screens.CategorySelection.route)}

            )
        }
        composable(Screens.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screens.CategorySelection.route) {
            CategorySelectionScreen(
                onNavigateToBottomNav = {
                    navController.navigate(Graph.BOTTOM_NAV_GRAPH)
                }
            )
        }
        composable(Screens.VerificationCode.route) {
            VerificationCodeScreen(
                onNavigateToTutorial = { navController.navigate(Screens.Tutorial.route) },
                onNavigateToSignIn = { navController.navigate(Screens.SignIn.route) }
            )
        }
    }
}