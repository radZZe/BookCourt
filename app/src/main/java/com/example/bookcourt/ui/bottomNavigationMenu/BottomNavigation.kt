package com.example.bookcourt.ui.bottomNavigationMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bookcourt.ui.theme.*
import com.example.bookcourt.utils.BottomNavMenu
import com.example.bookcourt.utils.Constants
import com.example.bookcourt.utils.Constants.LIMIT_WINDOW_HEIGHT
import com.example.bookcourt.utils.Graph
import com.example.bookcourt.utils.Screens


@Composable
fun BottomNavigationMenu(
    navController: NavController,
    viewModel: BottomNavViewModel = hiltViewModel()) {
    val invalidScreens = listOf(
        Screens.Statistics.route,
        Screens.ProfileSettings.route,
        Screens.Support.route,
        Screens.AboutApp.route,
        Screens.OrdersNotifications.route,
        Screens.Orders.route,
        Screens.AskQuestion.route,
        Screens.CategorySelection.route,
        Screens.OrderingScreen.route ,
        Screens.SuccessPurchase.route
    )
    LaunchedEffect(key1 = Unit) {
        viewModel.getBasketSize()
    }
    val basketSize = viewModel.basketSize.collectAsState().value
    val windowHeight =
        LocalConfiguration.current.screenHeightDp.toFloat() * LocalDensity.current.density
    val menuHeight = if (windowHeight > LIMIT_WINDOW_HEIGHT) 72.dp else 56.dp
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
            visibility = if (invalidScreens.contains(currentRoute)) {
                0.dp
            } else {
                menuHeight
            }
            Constants.navMenuScreensList.forEach { screen ->
                CustomBottomNavigationItem(
                    basketSize = basketSize,
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
    basketSize: Int,
    contentColor: Color,
    screen: BottomNavMenu,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background = if (isSelected) SelectedMenuItem else MenuBackGround
    val icon = if (isSelected) screen.iconSelected else screen.icon
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .wrapContentWidth()
            .fillMaxHeight()
            .clickable(
                onClick = { onClick() },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
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
            if (screen == BottomNavMenu.Basket && basketSize != 0) {
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .align(Alignment.TopEnd)
                        .zIndex(2f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.Red),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = basketSize.toString(), color = Color.White)
                    }
                }

            }
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