package com.example.bookcourt.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bookcourt.ui.theme.*


@Composable
fun BottomNavigationMenu(navController: NavController) {
    var visibility by remember {
        mutableStateOf(1f)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(visibility)
            .background(MenuBackGround)
    ) {
        BottomNavigation(
            backgroundColor = MenuBackGround,
            contentColor = TextPlaceHolderColor,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            visibility = if (currentRoute == BottomNavMenu.Search.route) {
                0f
            } else {
                1f
            }
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
    val background = if (isSelected) SelectedMenuItem else MenuBackGround

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