package com.example.bookcourt.ui.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.bookcourt.ui.profile.ProfileScreen
import com.example.bookcourt.utils.Graph
import com.example.bookcourt.utils.Screens

fun NavGraphBuilder.profileNavGraph(navController: NavHostController){
    navigation(
        route = Graph.PROFILE_NAV_GRAPH,
        startDestination = Screens.Profile.route
    ){
        composable(Screens.Profile.route) {
            ProfileScreen(
                onNavigateToRecommendation = { navController.navigate(Graph.BOTTOM_NAV_GRAPH) }
            )
        }
    }

}