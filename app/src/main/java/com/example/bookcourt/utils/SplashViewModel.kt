package com.example.bookcourt.utils

import androidx.lifecycle.ViewModel
import com.example.bookcourt.data.repositories.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel  @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val rememberMeState = dataStoreRepository.getBoolPref(DataStoreRepository.isRemembered)
    val rememberTutorState = dataStoreRepository.getBoolPref(DataStoreRepository.isTutorChecked)
}