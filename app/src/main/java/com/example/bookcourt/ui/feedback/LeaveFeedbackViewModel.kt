package com.example.bookcourt.ui.feedback

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LeaveFeedbackViewModel @Inject constructor():ViewModel() {
    val feedbackText = mutableStateOf("")
    val feedbackIsConfirm = mutableStateOf(false)

}