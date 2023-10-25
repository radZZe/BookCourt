package com.example.bookcourt.utils

import androidx.lifecycle.ViewModel
import com.example.bookcourt.data.repositories.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel  @Inject constructor(
    dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val tutorState = dataStoreRepository.getBoolPref(DataStoreRepository.isTutorChecked)
    val categorySelectionState = dataStoreRepository.getBoolPref(DataStoreRepository.isCategoriesSelected)

}