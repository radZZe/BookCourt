package com.example.bookcourt.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.room.user.UserRepositoryI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AskQuestionViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userRepositoryI: UserRepositoryI
) : ViewModel() {
    var question by mutableStateOf("")

    fun onQuestionChanged(newText: String) {
        question = newText
    }
}