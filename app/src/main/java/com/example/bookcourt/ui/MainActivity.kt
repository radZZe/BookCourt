package com.example.bookcourt.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookcourt.data.BackgroundService
import com.example.bookcourt.ui.auth.SignInViewModel
import com.example.bookcourt.ui.graphs.NavigationGraph
import com.example.bookcourt.ui.theme.BookCourtTheme
import com.example.bookcourt.utils.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var bgService: BackgroundService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bgService.job()
        setContent {
            BookCourtTheme {
                val navController: NavHostController = rememberNavController()
                val splashScreenViewModel: SplashViewModel by viewModels()
                val signInScreenViewModel: SignInViewModel by viewModels()
                Scaffold(
                    bottomBar = { com.example.bookcourt.utils.BottomNavigation(navController = navController) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        NavigationGraph(navController, splashScreenViewModel, signInScreenViewModel)
                    }
                }

            }
        }
    }
}




