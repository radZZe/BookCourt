package com.example.bookcourt.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.bookcourt.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Recomendations : BottomBarScreen(
        route = "RECOMENDATIONS",
        title = "RECOMENDATIONS",
        icon = R.drawable.ic_baseline_local_fire_department_24
    )

    object Profile : BottomBarScreen(
        route = "PROFILE",
        title = "PROFILE",
        icon = R.drawable.ic_baseline_account_circle_24
    )

    object Library : BottomBarScreen(
        route = "LIBRARY",
        title = "LIBRARY",
        icon = R.drawable.ic_baseline_library_books_24
    )
    object ReadBook : BottomBarScreen(
        route = "READ_BOOK",
        title = "READ_BOOK",
        icon = R.drawable.ic_baseline_menu_book_24
    )
    object AddBook : BottomBarScreen(
        route = "ADD_BOOK",
        title = "ADD_BOOK",
        icon = R.drawable.ic_baseline_collections_bookmark_24
    )
    object Cart : BottomBarScreen(
        route = "CART",
        title = "CART",
        icon = R.drawable.ic_baseline_add_shopping_cart_24
    )
}