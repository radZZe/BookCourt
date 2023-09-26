//package com.example.bookcourt.ui.graphs
//
//import androidx.navigation.*
//import androidx.navigation.compose.composable
//import com.example.bookcourt.ui.BookCardScreen
//import com.example.bookcourt.ui.feedback.LeaveFeedbackScreen
//import com.example.bookcourt.ui.feedback.ListOfFeedbacksScreen
//import com.example.bookcourt.ui.profile.ProfileScreen
//import com.example.bookcourt.utils.Graph
//import com.example.bookcourt.utils.Screens
//
//fun NavGraphBuilder.bookCardGraph(navController: NavHostController) {
//    navigation(
//        route = Graph.BOOK_CARD_GRAPH,
//        startDestination = "${Screens.BookCardScreen.route}/{book}",
//    ) {
//        composable(
//            "${Screens.BookCardScreen.route}/{book}", arguments = listOf(
//                navArgument("book") { type = NavType.StringType })
//        ) {
//            val arguments = requireNotNull(it.arguments)
//            val book = arguments.getString("book", "not found")
//            BookCardScreen(
//                book = book,
//                onNavigateBack = { navController.popBackStack() },
//                onNavigateLeaveFeedback = { title, rate ->
//                    navController.navigate("${Screens.LeaveFeedback.route}/$title/$rate") },
//                onNavigateListFeedbacks = { title ->
//                    navController.navigate("${Screens.FeedbackBlock.route}/$title") }
//            )
//        }
//        composable(route = "${Screens.LeaveFeedback.route}/{title}/{rate}", arguments = listOf(
//            navArgument("title") { type = NavType.StringType },
//            navArgument("rate") { type = NavType.IntType }
//        )) { backStackEntry ->
//            val arguments = requireNotNull(backStackEntry.arguments)
//            val title = arguments.getString("title", "not found")
//            val rate = arguments.getInt("rate", 0)
//            LeaveFeedbackScreen(title = title, rate = rate,
//                onNavigateToRecommendationScreen = { description, needToUpdate ->
//                    navController.previousBackStackEntry?.savedStateHandle?.set(
//                        "description",
//                        description
//                    )
//                    navController.previousBackStackEntry?.savedStateHandle?.set(
//                        "needToUpdate",
//                        needToUpdate
//                    )
//                    navController.popBackStack()
//                })
//        }
//        composable(
//            route = "${Screens.FeedbackBlock.route}/{title}", arguments = listOf(
//                navArgument("title") { type = NavType.StringType },
//            )
//        ) { backStackEntry ->
//            val arguments = requireNotNull(backStackEntry.arguments)
//            val title = arguments.getString("title", "not found")
//            ListOfFeedbacksScreen(
//                title = title,
//                onNavigateToRecommendationScreen = { navController.navigate(Graph.BOTTOM_NAV_GRAPH) }
//            )
//        }
//    }
//
//}