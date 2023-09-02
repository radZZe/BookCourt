package com.example.bookcourt.ui.library

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.models.categorySelection.Category
import com.example.bookcourt.utils.Constants.genres
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    networkRepository: NetworkRepository
):ViewModel() {
    val categories = genres.map { mutableStateOf(Category(it, mutableStateOf(false))) }
        .toMutableStateList()
}