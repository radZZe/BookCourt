package com.example.bookcourt.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bookcourt.ui.theme.*


@Preview
@Composable
fun menu() {
    val context = LocalContext.current
    val navController = NavController(context)
    BottomNavigationMenu(navController = navController)
}

@Composable
fun BottomNavigationMenu(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackGroundWhite)
    ) {
        BottomNavigation(
            backgroundColor = BackGroundWhite,
            contentColor = TextPlaceHolderColor,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            Constants.navMenuScreensList.forEach { screen ->
                CustomBottomNavigationItem(
                    contentColor = AppLogoBlack,
                    screen = screen,
                    isSelected = (screen.route == currentRoute)
                ) {
                    navController.navigate(screen.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
//                BottomNavigationItem(
//                    icon = {
//                        Icon(
//                            painter = painterResource(id = screen.icon),
//                            contentDescription = "bnmIcon",
//                            tint = AppLogoBlack
//                        )
//                    },
//                    selected = (screen.route == currentRoute),
//                    selectedContentColor = AppLogoBlack,
//                    unselectedContentColor = AppLogoBlack,
//                    label = {
//                        Text(
//                            text = screen.title,
//                            fontSize = 12.sp,
//                            fontFamily = Roboto,
//                            fontWeight = FontWeight.Medium,
//                            modifier = Modifier.padding(top = 8.dp)
//                        )
//                    },
//                    alwaysShowLabel = true,
//                    onClick = {
//                        navController.navigate(screen.route) {
//                            navController.graph.startDestinationRoute?.let { route ->
//                                popUpTo(route) {
//                                    saveState = true
//                                }
//                            }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    }
//                )
            }
        }
    }
}

@Composable
fun CustomBottomNavigationItem(
    contentColor: Color,
    screen: BottomNavMenu,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background = if (isSelected) SelectedMenuItem else BackGroundWhite

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(vertical = 2.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(background)
                .wrapContentHeight()
                .wrapContentWidth()
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = "cnbIcon",
                tint = contentColor,
                modifier = Modifier
                    .padding(vertical = 3.dp, horizontal = 22.dp)
                    .size(24.dp)
            )
        }
        Text(
            text = screen.title,
            fontSize = 12.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 2.dp, bottom = 0.dp),
            color = contentColor
        )
    }

}

//@Composable
//fun BottomNavigation(navController: NavController) {
//    val items = listOf(
//        BottomBarScreen.Library,
//        BottomBarScreen.Profile,
////        BottomBarScreen.Recomendations,
//        BottomBarScreen.ReadBook,
//        BottomBarScreen.AddBook,
//        BottomBarScreen.Cart,
//    )
//    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//        Card(
//            elevation = 6.dp,
//            shape = RoundedCornerShape(15.dp),
//            modifier = Modifier
//                .fillMaxWidth(0.9f)
//                .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 20.dp)
//        ) {
//            androidx.compose.material.BottomNavigation(
//                backgroundColor = colorResource(id = R.color.main_color),
//                contentColor = Color.Black
//            ) {
//                val navBackStackEntry by navController.currentBackStackEntryAsState()
//                val currentRoute = navBackStackEntry?.destination?.route
//                items.forEach { item ->
//                    BottomNavigationItem(
//                        icon = {
//                            Icon(
//                                painterResource(id = item.icon),
//                                contentDescription = item.title,
//                            )
//                        },
//                        selectedContentColor = Color.White,
//                        unselectedContentColor = colorResource(id = R.color.unselected),
//                        alwaysShowLabel = true,
//                        selected = currentRoute == item.route,
//                        onClick = {
//                            navController.navigate(item.route) {
//                                navController.graph.startDestinationRoute?.let { screen_route ->
//                                    popUpTo(screen_route) {
//                                        saveState = true
//                                    }
//                                }
//                                launchSingleTop = true
//                                restoreState = true
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    }
//
//
//}