package com.example.bookcourt.ui.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookcourt.ui.RecomendationScreen
import com.example.bookcourt.ui.statistics.Statistics
import com.example.bookcourt.utils.BottomNavMenu
import com.example.bookcourt.utils.Screens

@Composable
fun BottomNavigationGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavMenu.RecomendationsBottomNav.route
    ) {
        composable(route = BottomNavMenu.RecomendationsBottomNav.route) {
            RecomendationScreen(
                onNavigateToStatistics = {  }
            )
        }
        composable(route = BottomNavMenu.SearchBottomNav.route) {

        }
        composable(route = BottomNavMenu.StatisticsBottomNav.route) {
            Statistics(navController = navController)
        }
    }
}