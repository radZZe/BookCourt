package com.example.bookcourt.ui.graphs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookcourt.ui.library.LibraryScreen
import com.example.bookcourt.ui.profile.ProfileScreen
import com.example.bookcourt.ui.recommendation.RecommendationScreen
import com.example.bookcourt.ui.search.SearchScreen
import com.example.bookcourt.ui.statistics.Statistics
import com.example.bookcourt.utils.BottomNavMenu
import com.example.bookcourt.utils.BottomNavigationMenu
import com.example.bookcourt.utils.Graph
import com.example.bookcourt.utils.Screens

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomNavigationMenu(navController) }
    ) { padding->
        Box(modifier = Modifier.padding(padding)){
            BottomNavigationGraph(navController = navController)
        }

    }
}

@Composable
fun BottomNavigationGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        route = Graph.BOTTOM_NAV_GRAPH,
        startDestination = BottomNavMenu.Recommendations.route
    ) {
        composable(BottomNavMenu.Recommendations.route) {
            BackHandler(true) {}
            RecommendationScreen(
                onNavigateToStatistics = { navController.navigate(BottomNavMenu.Statistics.route) },
                onNavigateToProfile = {navController.navigate(Graph.PROFILE_NAV_GRAPH)}
            )
        }
        composable(route = BottomNavMenu.Bag.route) {
            SearchScreen(
                onNavigateBack = {navController.popBackStack()}
            )
        }
        composable(route = BottomNavMenu.Library.route){
            LibraryScreen(
                onNavigateToSearchScreen = {navController.navigate(Screens.Search.route)}
            )
        }
        composable(Screens.Search.route) {
            SearchScreen(
                onNavigateBack = {navController.popBackStack()}
            )
        }
        composable(route = BottomNavMenu.Profile.route){
            ProfileScreen(onNavigateToRecommendation = { navController.navigate(Graph.BOTTOM_NAV_GRAPH)})
        }
    }
}