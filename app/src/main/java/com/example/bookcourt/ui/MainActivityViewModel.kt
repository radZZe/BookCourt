package com.example.bookcourt.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.startSessionTime
import com.example.bookcourt.data.repositories.MetricsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val metricRep: MetricsRepository
) : ViewModel() {

    fun getStartSessionTime() {
        val startSessionTimeUnix = System.currentTimeMillis().toInt()
        viewModelScope.launch {
            dataStoreRepository.setPref(startSessionTimeUnix, startSessionTime)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setSessionLengthTime() {
        val endSessionTime = System.currentTimeMillis().toInt()
        viewModelScope.launch {
            dataStoreRepository.getIntPref(startSessionTime).collect { startTime ->
                metricRep.appTime(endSessionTime - startTime)
            }
        }
    }

}