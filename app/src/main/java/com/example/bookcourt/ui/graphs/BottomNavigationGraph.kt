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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookcourt.ui.basket.basketScreen.BasketScreen
import com.example.bookcourt.ui.BookCardScreen
import com.example.bookcourt.ui.basket.SuccessPurchaseScreen
import com.example.bookcourt.ui.basket.orderingScreen.OrderingScreen
import com.example.bookcourt.ui.bottomNavigationMenu.BottomNavViewModel
import com.example.bookcourt.ui.categorySelection.CategorySelectionScreen2
import com.example.bookcourt.ui.feedback.LeaveFeedbackScreen
import com.example.bookcourt.ui.feedback.ListOfFeedbacksScreen
import com.example.bookcourt.ui.library.LibraryScreen
import com.example.bookcourt.ui.profile.*
import com.example.bookcourt.ui.recommendation.RecommendationScreen
import com.example.bookcourt.ui.search.SearchScreen
import com.example.bookcourt.ui.statistics.Statistics
import com.example.bookcourt.utils.BottomNavMenu
import com.example.bookcourt.ui.bottomNavigationMenu.BottomNavigationMenu
import com.example.bookcourt.utils.Graph
import com.example.bookcourt.utils.Screens

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomNavigationMenu(navController) }
    ) {
        val bottomPadding = it.calculateBottomPadding()
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = bottomPadding)){
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
        startDestination = BottomNavMenu.Library.route
    ) {
        composable(BottomNavMenu.Recommendations.route) {
            BackHandler(true) {}
            RecommendationScreen(
                onNavigateToStatistics = { navController.navigate(BottomNavMenu.Library.route) }, //TODO after the Library screen will be added it is necessary to change BottomNavMenu.Statistics.route or even change navGraph
                onNavigateToProfile = {navController.navigate(Graph.PROFILE_NAV_GRAPH)},
                onNavigateBookCard = {book-> navController.navigate("${Screens.BookCardScreen.route}/$book")}
            )
        }
        composable(route = BottomNavMenu.Basket.route) {
            BasketScreen(
                onNavigateToOrdering = { navController.navigate(Screens.OrderingScreen.route)}
            )

        }

        composable(route = BottomNavMenu.Library.route) {
            LibraryScreen(
                onNavigateToSearchScreen = { navController.navigate(Screens.Search.route) },
                onNavigateBookCard = {book-> navController.navigate("${Screens.BookCardScreen.route}/$book")}
            )
        }
        composable(route = BottomNavMenu.Profile.route) {
            ProfileScreen(
                onNavigateToStatistics = { navController.navigate(Screens.Statistics.route) },
                onNavigateToAbout = { navController.navigate(Screens.AboutApp.route) },
                onNavigateToSettings = { navController.navigate(Screens.ProfileSettings.route) },
                onNavigateToSupport = { navController.navigate(Screens.Support.route) },
                onNavigateToOrdersNotifications = { navController.navigate(Screens.OrdersNotifications.route) },
                onNavigateToWantToRead = { navController.navigate(Screens.WantToRead.route) },
                onNavigateToOrders = { navController.navigate(Screens.Orders.route) },
                onNavigateToCategorySelection = { navController.navigate(Screens.CategorySelection.route) }
            )
        }
        composable(route = Screens.Search.route){
            SearchScreen(
                onNavigateBack = {navController.popBackStack()}
            )
        }
//        composable(route = BottomNavMenu.Library.route) {
//            LibraryPlug(
//                onNavigateToLibrary = { navController.navigate(Graph.BOTTOM_NAV_GRAPH) }
//            )
//        }
        composable(route = BottomNavMenu.Profile.route){
            ProfileScreen(
                onNavigateToStatistics = { navController.navigate(Screens.Statistics.route) },
                onNavigateToAbout = { navController.navigate(Screens.AboutApp.route) },
                onNavigateToSettings = { navController.navigate(Screens.ProfileSettings.route) },
                onNavigateToSupport = { navController.navigate(Screens.Support.route) },
                onNavigateToOrdersNotifications = { navController.navigate(Screens.OrdersNotifications.route) },
                onNavigateToWantToRead = { navController.navigate(Screens.WantToRead.route) },
                onNavigateToOrders = { navController.navigate(Screens.Orders.route) },
                onNavigateToCategorySelection = { navController.navigate(Screens.CategorySelection.route) })

        }

        composable(
            "${Screens.BookCardScreen.route}/{book}"
        ) {
            val feedbackText = it.savedStateHandle.get<String>("description")
            val rate = it.savedStateHandle.get<Int>("rate")
            val arguments = requireNotNull(it.arguments)
            val bookId = arguments.getString("book", "not found")

            val needToUpdate = it.savedStateHandle.get<Boolean>("needToUpdate")
            BookCardScreen(
                bookId = bookId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateLeaveFeedback = { title, rate ->
                    navController.navigate("${Screens.LeaveFeedback.route}/$title/$rate") },
                onNavigateListFeedbacks = { title ->
                    navController.navigate("${Screens.FeedbackBlock.route}/$title") },
                feedbackText = feedbackText,
                needToUpdate = needToUpdate ?: false,
                newRate = rate ?:null
            )
        }
        composable(route = "${Screens.LeaveFeedback.route}/{title}/{rate}", arguments = listOf(
            navArgument("title") { type = NavType.StringType },
            navArgument("rate") { type = NavType.IntType }
        )) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val title = arguments.getString("title", "not found")
            val rate = arguments.getInt("rate", 0)
            LeaveFeedbackScreen(title = title, rate = rate,
                onBackNavigation = { description, needToUpdate,rate ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "description",
                        description
                    )
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "needToUpdate",
                        needToUpdate
                    )
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "rate",
                        rate
                    )
                    navController.popBackStack()
                })
        }
        composable(
            route = "${Screens.FeedbackBlock.route}/{title}", arguments = listOf(
                navArgument("title") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val title = arguments.getString("title", "not found")
            ListOfFeedbacksScreen(
                title = title,
                onNavigateToRecommendationScreen = { navController.navigate(Graph.BOTTOM_NAV_GRAPH) }
            )
        }

        composable(
            route = "${Screens.OrderingScreen.route}"
        ) {
            OrderingScreen(onBackNavigation = {navController.popBackStack()}, onSuccessPurchaseNavigate =
            {
                navController.navigate(Screens.SuccessPurchase.route)
            })
        }

        composable(route = Screens.AboutApp.route) {
            AboutApp(
                onNavigateToProfile = { navController.navigate(Screens.Profile.route) }
            )
        }

        composable(route = Screens.ProfileSettings.route) {
            ProfileSettings(onNavigateToProfile = { navController.navigate(Screens.Profile.route) })
        }

        composable(route = Screens.Support.route) {
            Support(
                onNavigateToProfile = { navController.navigate(Screens.Profile.route) },
                onNavigateToAskQuestion = { navController.navigate(Screens.AskQuestion.route) }
            )
        }

        composable(route = Screens.Statistics.route) {
            Statistics(onNavigateToProfile = { navController.navigate(Screens.Profile.route) })
        }

        composable(route = Screens.OrdersNotifications.route) {
            OrdersNotificationsScreen(onNavigateToProfile = { navController.navigate(Screens.Profile.route) })
        }

        composable(route = Screens.WantToRead.route) {
            WantToReadScreen(onNavigateToProfile = { navController.navigate(Screens.Profile.route) })
        }

        composable(route = Screens.Orders.route) {
            OrdersScreen(onNavigateToProfile = { navController.navigate(Screens.Profile.route) })
        }

        composable(route = Screens.AskQuestion.route) {
            AskQuestionScreen(onNavigateToSupport = { navController.navigate(Screens.Support.route) })
        }

        composable(route = Screens.CategorySelection.route) {
            CategorySelectionScreen2(onNavigateToProfile = { navController.navigate(Screens.Profile.route) })
        }
        composable(route = "${Screens.SuccessPurchase.route}"){
            SuccessPurchaseScreen(onNavigateBack = {navController.popBackStack()})
        }

    }
}
