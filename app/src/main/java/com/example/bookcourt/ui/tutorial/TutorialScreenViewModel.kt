package com.example.bookcourt.ui.tutorial

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepositoryImpl
import com.example.bookcourt.models.metrics.DataClickMetric
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val metricRep: MetricsRepositoryImpl,
) : ViewModel() {
    var currentCardState by mutableStateOf(false)
    var isTutorChecked by mutableStateOf(false)

    fun changeIsTutorChecked() {
        isTutorChecked = !isTutorChecked
    }

//    fun changeState() {
//        currentCardState = !currentCardState
//    }

    private suspend fun editPrefs() {
        dataStoreRepository.setPref(isTutorChecked, DataStoreRepository.isTutorChecked)
    }

    fun metricClick(clickMetric: DataClickMetric) {
        viewModelScope.launch(Dispatchers.IO) {
            editPrefs()
            metricRep.onClick(clickMetric)
        }
    }

}