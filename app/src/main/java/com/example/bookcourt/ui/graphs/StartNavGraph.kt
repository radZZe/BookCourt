package com.example.bookcourt.ui.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.bookcourt.ui.categorySelection.CategorySelectionRegistrationScreen
import com.example.bookcourt.ui.tutorial.TutorScreen
import com.example.bookcourt.utils.Graph
import com.example.bookcourt.utils.Screens
import com.example.bookcourt.utils.SplashScreen

fun NavGraphBuilder.startNavGraph(navController: NavHostController){
    navigation(
        route = Graph.START,
        startDestination = Screens.Splash.route
    ){
        composable(Screens.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screens.Tutorial.route) {
            TutorScreen(
                onNavigateToCategorySelection = {
                    navController.navigate(Screens.CategorySelectionRegistrationScreen.route)
                }
            )
        }

        composable(Screens.CategorySelectionRegistrationScreen.route) {
            CategorySelectionRegistrationScreen(
                onNavigateToLibrary = {
                    navController.navigate(Graph.BOTTOM_NAV_GRAPH)
                }
            )
        }
    }

}