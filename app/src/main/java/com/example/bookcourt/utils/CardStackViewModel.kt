package com.example.bookcourt.utils

import androidx.lifecycle.ViewModel
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardStackViewModel @Inject constructor(
    val dataStoreRepository: DataStoreRepository
) : ViewModel() {

}