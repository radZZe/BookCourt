package com.example.bookcourt.ui.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.isRemembered
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedName
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedPhoneNumber
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedSurname
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.uuid
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.utils.Hashing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val metricRep:MetricsRepository,
    private val hashing:Hashing
) : ViewModel() {

    var name by mutableStateOf("")
    var surname by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var isRememberMe by mutableStateOf(false)


    fun onCheckedChanged() {
        isRememberMe = !isRememberMe
    }

    fun onNameChanged(newText: String) {
        name = newText
    }

    fun onSurnameChanged(newText: String) {
        surname = newText
    }

    fun onPhoneChanged(newText: String) {
        phoneNumber = newText
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun editPrefs() {
        viewModelScope.launch {
            var value = "AB"+name+phoneNumber
            var UUID = hashing.getHash(value.toByteArray(),"SHA256")
            metricRep.sendUserData(name,surname,phoneNumber,UUID)
            dataStoreRepository.setPref(surname, savedSurname)
            dataStoreRepository.setPref(name, savedName)
            dataStoreRepository.setPref(phoneNumber, savedPhoneNumber)
            dataStoreRepository.setPref(isRememberMe, isRemembered)
            dataStoreRepository.setPref(UUID,uuid)
        }

    }
//    fun signIn(onSuccess: () -> Unit) {
//        if (isRememberMe) {
//            editPrefs()
//        }
//    }
}