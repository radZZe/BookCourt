package com.example.bookcourt.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.bookcourt.ui.graphs.RootNavigationGraph
import com.example.bookcourt.ui.theme.BookCourtTheme
import com.example.bookcourt.data.observers.ConnectivityObserver
import com.example.bookcourt.data.observers.ConnectivityObserverI
import com.example.bookcourt.ui.error_pages.ErrorPage
import com.example.bookcourt.ui.error_pages.ErrorType
import com.example.bookcourt.utils.MapApi
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val connectivityObserver = ConnectivityObserver(applicationContext)
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(MapApi.API_KEY)
        MapKitFactory.initialize(this)
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContent {
            val status by connectivityObserver.observe().collectAsState(initial =ConnectivityObserverI.Status.Available)
            BookCourtTheme {
                Scaffold(
                ) { it->
                    val bottomPadding = it.calculateBottomPadding()
                    if(status == ConnectivityObserverI.Status.Available ){
                        Column(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            RootNavigationGraph(navController = rememberNavController())
                        }
                    }else{
                        ErrorPage(ErrorType.Internet)
                    }


                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mViewModel.startTimer()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        mViewModel.sendMetric()
        MapKitFactory.getInstance().onStop()
    }

}






