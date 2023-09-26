package com.example.bookcourt.ui.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookcourt.utils.Graph

@Composable
fun RootNavigationGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.LOGIN
    ) {
        loginNavGraph(navController = navController)
        composable(route = Graph.BOTTOM_NAV_GRAPH) {
            HomeScreen()
        }
        profileNavGraph(navController = navController)
        //bookCardGraph(navController = navController)
    }
}