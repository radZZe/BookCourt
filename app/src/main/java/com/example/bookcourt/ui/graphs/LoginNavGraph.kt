package com.example.bookcourt.ui.graphs

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.bookcourt.ui.auth.SignInScreen
import com.example.bookcourt.ui.categorySelection.CategorySelectionRegistrationScreen
import com.example.bookcourt.ui.categorySelection.CategorySelectionScreen
import com.example.bookcourt.ui.tutorial.TutorScreen
import com.example.bookcourt.ui.verification.VerificationCodeScreen
import com.example.bookcourt.utils.Graph
import com.example.bookcourt.utils.Screens
import com.example.bookcourt.utils.SplashScreen

fun NavGraphBuilder.loginNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.LOGIN,
        startDestination = Screens.SignIn.route
    ) {


//        composable(Screens.SignIn.route) {
//            SignInScreen(
//                onNavigateToVerificationCode = {navController.navigate(Screens.VerificationCode.route)},
////                onNavigateToCategorySelection = {navController.navigate(Screens.CategorySelectionRegistrationScreen.route)}
//            )
//        }
//
//        composable(Screens.VerificationCode.route) {
//            VerificationCodeScreen(
//                onNavigateToTutorial = { navController.navigate(Graph.BOTTOM_NAV_GRAPH) },
//                onNavigateToSignIn = { navController.navigate(Screens.SignIn.route) }
//            )
//        }
    }
}