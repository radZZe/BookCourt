package com.example.bookcourt.ui.graphs

import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookcourt.ui.feedback.LeaveFeedbackScreen
import com.example.bookcourt.ui.feedback.ListOfFeedbacksScreen
import com.example.bookcourt.ui.library.LibraryScreen
import com.example.bookcourt.ui.profile.ProfileScreen
import com.example.bookcourt.ui.recommendation.RecommendationScreen
import com.example.bookcourt.ui.search.SearchScreen
import com.example.bookcourt.ui.statistics.LibraryPlug
import com.example.bookcourt.ui.statistics.Statistics
import com.example.bookcourt.utils.BottomNavMenu
import com.example.bookcourt.utils.BottomNavigationMenu
import com.example.bookcourt.utils.Graph
import com.example.bookcourt.utils.Screens

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomNavigationMenu(navController) }
    ) {
        val bottomPadding = it.calculateBottomPadding()
        Box(modifier = Modifier.fillMaxSize().padding(bottom = bottomPadding)){
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
            val description = it.savedStateHandle.get<String>("description")
            val needToUpdate = it.savedStateHandle.get<Boolean>("needToUpdate")
            BackHandler(true) {}
            RecommendationScreen(
                onNavigateToStatistics = { navController.navigate(BottomNavMenu.Library.route) }, //TODO after the Library screen will be added it is necessary to change BottomNavMenu.Statistics.route or even change navGraph
                isNeedToUpdateFeedback = needToUpdate ?: false,
                description = description,
                onNavigateToLeaveFeedbackScreen = {title,rate -> navController.navigate("${Screens.LeaveFeedback.route}/$title/$rate")},
                onNavigateToFeedback = {title -> navController.navigate("${Screens.FeedbackBlock.route}/$title")},
                onNavigateToProfile = {navController.navigate(Graph.PROFILE_NAV_GRAPH)}
            )
        }
        composable(route = BottomNavMenu.Basket.route) {
            SuccessPurchaseScreen() {
                navController.popBackStack()
            }
        }
        composable(route = BottomNavMenu.Library.route){
            LibraryScreen(
                onNavigateToSearchScreen = {navController.navigate(Screens.Search.route)}
            )
        }
        composable(route = BottomNavMenu.Library.route) {
            LibraryPlug(
                onNavigateToLibrary = { navController.navigate(Graph.BOTTOM_NAV_GRAPH) }
            )
        }
        composable(route = BottomNavMenu.Profile.route){
            ProfileScreen(onNavigateToRecommendation = { navController.navigate(Graph.BOTTOM_NAV_GRAPH)})
        }
        composable(route = "${Screens.LeaveFeedback.route}/{title}/{rate}", arguments = listOf(
            navArgument("title") { type = NavType.StringType },
            navArgument("rate") { type = NavType.IntType }
        )){backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val title = arguments.getString("title", "not found")
            val rate = arguments.getInt("rate",0)
            LeaveFeedbackScreen(title = title ,rate = rate,
                onNavigateToRecommendationScreen = {description ,needToUpdate->
                    navController.previousBackStackEntry?.savedStateHandle?.set("description",description)
                    navController.previousBackStackEntry?.savedStateHandle?.set("needToUpdate",needToUpdate)
                    navController.popBackStack()})
        }
        composable(route = "${Screens.FeedbackBlock.route }/{title}", arguments = listOf(
            navArgument("title") { type = NavType.StringType },
        )){backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val title = arguments.getString("title", "not found")
            ListOfFeedbacksScreen(
                title = title,
                onNavigateToRecommendationScreen = { navController.navigate(Graph.BOTTOM_NAV_GRAPH)}
            )
        }
    }
}