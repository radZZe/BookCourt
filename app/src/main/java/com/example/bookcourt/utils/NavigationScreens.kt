package com.example.bookcourt.utils

import com.example.bookcourt.R


sealed class Screens(
    val route: String,
    val title: String,
) {
    object SignIn : Screens(route = "Sign In", title = "Sign In")
    object Splash : Screens(route = "Splash", title = "Splash")
    object Recommendation : Screens(route = "Recommendations", title = "Recommendations")
    object Tutorial : Screens(route = "Tutorial", title = "Tutorial")
}

sealed class BottomNavMenu(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Recommendations : BottomNavMenu(
        route = "Recommendations",
        title = "Рекомендации",
        icon = R.drawable.ic_recomendations
    )
    object Statistics : BottomNavMenu(
        route = "Statistics",
        title = "Статистика",
        icon = R.drawable.ic_statistics
    )
    object Search : BottomNavMenu(
        route = "Search",
        title = "Поиск",
        icon = R.drawable.ic_search
    )
}

object Graph {
    const val ROOT = "root_graph"
    const val LOGIN = "login_graph"
    const val BOTTOM_NAV_GRAPH = "bottom_nav_graph"
}