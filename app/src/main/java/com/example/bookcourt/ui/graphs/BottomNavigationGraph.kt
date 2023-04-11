package com.example.bookcourt.ui.graphs

import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookcourt.ui.ProfileScreen
import com.example.bookcourt.ui.recommendation.RecommendationScreen
import com.example.bookcourt.ui.search.SearchScreen
import com.example.bookcourt.ui.statistics.Statistics
import com.example.bookcourt.utils.BottomNavMenu
import com.example.bookcourt.utils.BottomNavigationMenu
import com.example.bookcourt.utils.Graph

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomNavigationMenu(navController) }
    ) {
        BottomNavigationGraph(navController = navController)
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
        composable(route = BottomNavMenu.Search.route) {
            SearchScreen(navController)
        }
        composable(route = BottomNavMenu.Statistics.route) {
            Statistics()
        }
        composable(route = Graph.PROFILE_NAV_GRAPH){
            ProfileScreen {
                navController.navigate(Graph.BOTTOM_NAV_GRAPH)
            }
        }
    }
}