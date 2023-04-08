package com.example.bookcourt.ui.graphs

import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookcourt.ui.RecomendationScreen
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
            RecomendationScreen(
                onNavigateToStatistics = { navController.navigate(BottomNavMenu.Statistics.route) }
            )
        }
        composable(route = BottomNavMenu.Search.route) {
            SearchScreen(navController)
        }
        composable(route = BottomNavMenu.Statistics.route) {
            Statistics()
        }
    }
}