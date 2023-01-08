package com.example.bookcourt.ui.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookcourt.ui.RecomendationScreen
import com.example.bookcourt.ui.ProfileScreen
import com.example.bookcourt.utils.BottomBarScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomBarScreen.Recomendations.route) {
        composable(BottomBarScreen.Library.route) {
//            LibraryScreen()
        }
        composable(BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
        composable(BottomBarScreen.Recomendations.route) {
            RecomendationScreen()
        }
        composable(BottomBarScreen.ReadBook.route) {
//            ReadBookScreen()
        }
        composable(BottomBarScreen.AddBook.route) {
//           AddBookScreen()
        }
        composable(BottomBarScreen.Cart.route) {
//            CartScreen()
        }
    }
}