package com.example.bookcourt.ui.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.bookcourt.ui.profile.AboutApp
import com.example.bookcourt.ui.profile.ProfileScreen
import com.example.bookcourt.ui.profile.Support
import com.example.bookcourt.utils.Graph
import com.example.bookcourt.utils.Screens

fun NavGraphBuilder.profileNavGraph(navController: NavHostController){
    navigation(
        route = Graph.PROFILE_NAV_GRAPH,
        startDestination = Screens.Profile.route
    ){
//        composable(route = Screens.Profile.route) {
//            ProfileScreen(
//                onNavigateToAbout = { navController.navigate(Graph.PROFILE_NAV_GRAPH) },
//                onNavigateToSettings = { navController.navigate(Screens.ProfileSettings.route) },
//                onNavigateToSupport = { navController.navigate(Screens.Support.route) }
//            )
//        }

        composable(route = Screens.AboutApp.route) {
            AboutApp(
                onNavigateToProfile = { navController.navigate(Screens.Profile.route) }
            )
        }

//        composable(route = Screens.ProfileSettings.route) {
//            ProfileScreen()
//        }

//        composable(route = Screens.Support.route) {
//            Support(onNavigateToProfile = {navController.navigate(Screens.Profile.route)})
//        }
    }

}