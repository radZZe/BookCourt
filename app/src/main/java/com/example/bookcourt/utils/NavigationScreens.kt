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
    object Tutorial : Screens(route = "Tutorial", title = "Tutorial")
}

sealed class BottomNavMenu(
    val route: String,
    val title: String,
    val icon: Int
) {
    object RecomendationsBottomNav : BottomNavMenu(
        route = "Recommendations",
        title = "Рекомендации",
        icon = R.drawable.ic_recomendations
    )
    object StatisticsBottomNav : BottomNavMenu(
        route = "Statistics",
        title = "Статистика",
        icon = R.drawable.ic_statistics
    )
    object SearchBottomNav : BottomNavMenu(
        route = "Search",
        title = "Поиск",
        icon = R.drawable.ic_search
    )
}