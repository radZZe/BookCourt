package com.example.bookcourt.utils

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.Constants.LIMIT_WINDOW_HEIGHT


@Composable
fun BottomNavigationMenu(navController: NavController) {
    val windowHeight = LocalConfiguration.current.screenHeightDp.toFloat() * LocalDensity.current.density
    val menuHeight = if(windowHeight > LIMIT_WINDOW_HEIGHT) 72.dp else 56.dp

//    var visibility by remember {
//        mutableStateOf(1f)
//    }
    var visibility by remember {
        mutableStateOf(menuHeight)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(visibility)
            .background(MenuBackGround)
    ) {
        BottomNavigation(
            backgroundColor = MenuBackGround,
            contentColor = TextPlaceHolderColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(menuHeight),
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            visibility = if (currentRoute == Screens.Search.route || currentRoute == Graph.PROFILE_NAV_GRAPH) {
                0.dp
            } else {
                menuHeight
            }
            Constants.navMenuScreensList.forEach { screen ->
                CustomBottomNavigationItem(
                    contentColor = AppLogoBlack,
                    screen = screen,
                    isSelected = (screen.route == currentRoute)
                ) {
                    navController.navigate(screen.route) {
                        launchSingleTop = true
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
    val icon = if (isSelected) screen.iconSelected else screen.icon
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .clickable(
                onClick = { onClick() },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .padding(vertical = 2.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(background)
                .wrapContentHeight()
                .wrapContentWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
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