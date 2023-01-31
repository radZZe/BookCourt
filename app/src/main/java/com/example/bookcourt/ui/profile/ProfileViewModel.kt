package com.example.bookcourt.ui.profile

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedCity
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedName
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedPhoneNumber
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedSurname
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.models.User
import com.example.bookcourt.models.UserRemote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repository: NetworkRepository,
    val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    val user = mutableStateOf<User?>(null)
    val alertDialogState = mutableStateOf(false)
    val feedbackState = mutableStateOf(false)
    val statisticsState = mutableStateOf(false)
    val feedbackData = mutableStateOf("")
    val feedbackMessage = mutableStateOf("")

    val userName = dataStoreRepository.getPref(savedName)
    val userSurname = dataStoreRepository.getPref(savedSurname)
    val userPhone = dataStoreRepository.getPref(savedPhoneNumber)
    val userCity = dataStoreRepository.getPref(savedCity)

    fun dismiss() {
        if (feedbackState.value) {
            feedbackStateChanged()
        } else {
            statisticsStateChanged()
        }
    }

    fun statisticsStateChanged() {
        statisticsState.value = !statisticsState.value
        alertDialogState.value = !alertDialogState.value
    }

    fun feedbackStateChanged() {
        feedbackState.value = !feedbackState.value
        alertDialogState.value = !alertDialogState.value
    }

    fun feedbackDataChanged(str: String) {
        feedbackData.value = str
    }

    fun feedbackMessageChanged(str: String) {
        feedbackMessage.value = str
    }

    fun getUserData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val job = async { repository.getUserData(context) }
            val json = job.await()
            val data = Json.decodeFromString<UserRemote>("""$json""")
            user.value = data.toUser()
        }
    }
}