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
import com.example.bookcourt.utils.Hashing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    val categories = mutableStateListOf<MutableState<Category>>(
        mutableStateOf(
            Category("Детективы", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Детская литература", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Рассказы", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Фантастика", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Экономика", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Научная фантастика", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Фэнтези", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Зарубежная литература", mutableStateOf(false))
        )
    )

    private var isCategoriesSelected by mutableStateOf(false)

    private suspend fun editPrefs() {
        dataStoreRepository.setPref(!isCategoriesSelected, DataStoreRepository.isCategoriesSelected)

        val UUID = hashing.getHash("AB".toByteArray(), "SHA256")
        val user = User(
            uid = UUID,
            email = "",
            city = "",
            readBooksList = mutableListOf(),
            wantToRead = mutableListOf()
        )
        userRepositoryI.saveData(user)
        dataStoreRepository.setPref(UUID, DataStoreRepository.uuid)
    }

    fun changeStateCategory(index: Int) {
        if (categories[index].value.state.value) {
            categories[index].value.state.value = !categories[index].value.state.value
            selectedCategories.remove(categories[index])
        } else {
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