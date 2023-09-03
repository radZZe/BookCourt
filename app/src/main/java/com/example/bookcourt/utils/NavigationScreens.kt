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
    object CategorySelection : Screens(route = "CategorySelection", title = "CategorySelection")
    object Profile:Screens(route = "Profile", title = "Profile")
    object Search : Screens(route = "Search", title = "Поиск")
    object VerificationCode: Screens(route = "VerificationCode", title = "VerificationCode")
}

sealed class BottomNavMenu(
    val route: String,
    val title: String,
    val icon: Int,
    val iconSelected: Int
) {
    object Recommendations : BottomNavMenu(
        route = "Recommendations",
        title = "Рекомендации",
        icon = R.drawable.ic_recomendations,
        iconSelected = R.drawable.ic_tinder_selected
    )
    object Statistics : BottomNavMenu(
        route = "Statistics",
        title = "Статистика",
        icon = R.drawable.ic_stats,
        iconSelected = R.drawable.ic_stats_selected
    )
    object Library: BottomNavMenu(
        route = "Library",
        title = "Библиотека",
        icon = R.drawable.ic_library,
        iconSelected = R.drawable.ic_library_selected
    )
}

object Graph {
    const val ROOT = "root_graph"
    const val LOGIN = "login_graph"
    const val BOTTOM_NAV_GRAPH = "bottom_nav_graph"
    const val PROFILE_NAV_GRAPH = "profile_nav_graph"
}