package com.example.bookcourt.ui.categorySelection

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.room.user.UserRepositoryI
import com.example.bookcourt.models.categorySelection.Category
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.models.user.User
import com.example.bookcourt.utils.Constants.genres
import com.example.bookcourt.utils.Hashing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategorySelectionViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val metricRep: MetricsRepository,
    private val userRepositoryI: UserRepositoryI,
    private val hashing: Hashing,
) : ViewModel() {
    val selectedCategories = mutableStateListOf<MutableState<Category>>()
    val categories =  genres.map {
            mutableStateOf(Category(it, mutableStateOf(false)))
        }.toMutableStateList()

    private var isCategoriesSelected by mutableStateOf(false)

    private suspend fun editPrefs() {
        dataStoreRepository.setPref(!isCategoriesSelected, DataStoreRepository.isCategoriesSelected)

        val UUID = hashing.getHash("AB".toByteArray(), "SHA256")
        val user = User(
            uid = UUID,
            email = "",
            surname = "",
            readBooksList = mutableListOf(),
            wantToRead = mutableListOf(),
            liked = mutableListOf()
        )
        userRepositoryI.saveData(user)
        dataStoreRepository.setPref(UUID, DataStoreRepository.uuid)
    }

    fun changeStateCategory(index: Int) {
        if (selectedCategories.size<5){
            if(categories[index].value.isSelected.value){
                categories[index].value.isSelected.value = !categories[index].value.isSelected.value
                selectedCategories.remove(categories[index])
            }else{
                categories[index].value.isSelected.value = !categories[index].value.isSelected.value
                selectedCategories.add(categories[index])
            }
        }
        else{
            if(categories[index].value.isSelected.value){
                categories[index].value.isSelected.value = !categories[index].value.isSelected.value
                selectedCategories.remove(categories[index])
            }
        }
    }

    fun metricClick(clickMetric: DataClickMetric) {
        viewModelScope.launch(Dispatchers.IO) {
            editPrefs()
            metricRep.onClick(clickMetric)
        }
    }

}