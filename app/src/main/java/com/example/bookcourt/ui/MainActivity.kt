package com.example.bookcourt.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.startSessionTime
import com.example.bookcourt.ui.auth.SignInViewModel
import com.example.bookcourt.ui.graphs.NavigationGraph
import com.example.bookcourt.ui.theme.BookCourtTheme
import com.example.bookcourt.utils.Screens
import com.example.bookcourt.utils.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onStart() {
        super.onStart()
        val mainActivityViewModel: MainActivityViewModel by viewModels()
        mainActivityViewModel.getStartSessionTime()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStop() {
        super.onStop()
        val mainActivityViewModel: MainActivityViewModel by viewModels()
        mainActivityViewModel.setSessionLengthTime()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        val mainActivityViewModel: MainActivityViewModel by viewModels()
        mainActivityViewModel.setSessionLengthTime()
    }

}






