package com.example.bookcourt.utils

import com.example.bookcourt.R


sealed class Screens(
    val route: String,
    val title: String,
) {
    object SignIn : Screens(route = "Sign In", title = "Sign In")
    object Splash : Screens(route = "Splash", title = "Splash")
    object CardInfo : Screens(route = "Card Info", title = "Card Info")
    object Statistics : Screens(route = "Statistics", title = "Statistics")
    object Recommendation : Screens(route = "Recommendations", title = "Recommendations")
}

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
//    object Recomendations : BottomBarScreen(
//        route = "Recommendations",
//        title = "Recommendations",
//        icon = R.drawable.ic_baseline_local_fire_department_24
//    )

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