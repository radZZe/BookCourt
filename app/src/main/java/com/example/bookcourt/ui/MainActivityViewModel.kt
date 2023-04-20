package com.example.bookcourt.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepositoryImpl
import com.example.bookcourt.utils.MetricType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val metricRep: MetricsRepositoryImpl,
) : ViewModel() {

    var time = 0

    fun startTimer() {
        viewModelScope.launch(Dispatchers.Unconfined) {
            while (true) {
                delay(1000)
                time += 1000
            }
        }
    }

    fun sendMetric() {
        viewModelScope.launch(Dispatchers.IO) {
            metricRep.appTime(time, MetricType.APP_SESSION_TIME, "App")
            time = 0
        }
    }

}