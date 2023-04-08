package com.example.bookcourt.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.bookcourt.ui.graphs.RootNavigationGraph
import com.example.bookcourt.ui.theme.BookCourtTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookCourtTheme {
                Scaffold(
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        RootNavigationGraph(navController = rememberNavController())
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mViewModel.startTimer()
    }

    override fun onStop() {
        super.onStop()
        mViewModel.sendMetric()
    }

}






