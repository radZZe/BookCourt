package com.example.bookcourt.ui.categorySelection

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.models.categorySelection.Category
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.utils.Constants.genres
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySelectionViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val metricRep: MetricsRepository,
): ViewModel() {
    val selectedCategories = mutableStateListOf<MutableState<Category>>()
    val categories =  genres.map {
            mutableStateOf(Category(it, mutableStateOf(false)))
        }.toMutableStateList()

    private var isCategoriesSelected by mutableStateOf(false)

    private suspend fun editPrefs() {
        dataStoreRepository.setPref(!isCategoriesSelected, DataStoreRepository.isCategoriesSelected)
    }

    fun changeStateCategory(index: Int) {
        if(categories[index].value.state.value){
            categories[index].value.state.value = !categories[index].value.state.value
            selectedCategories.remove(categories[index])
        }else{
            categories[index].value.state.value = !categories[index].value.state.value
            selectedCategories.add(categories[index])
        }
    }

    fun metricClick(clickMetric: DataClickMetric) {
        viewModelScope.launch(Dispatchers.IO) {
            editPrefs()
            metricRep.onClick(clickMetric)
        }
    }

}