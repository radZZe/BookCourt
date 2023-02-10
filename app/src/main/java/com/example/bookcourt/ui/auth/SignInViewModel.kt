package com.example.bookcourt.ui.auth

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.isRemembered
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedCity
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedName
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedPhoneNumber
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedSurname
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.uuid
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.utils.Hashing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val metricRep: MetricsRepository,
    private val hashing: Hashing
) : ViewModel() {

    var name by mutableStateOf("")
    var surname by mutableStateOf("")
    var phoneNumber by mutableStateOf("+7")
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
                var value = "AB" + name + phoneNumber
                var UUID = hashing.getHash(value.toByteArray(), "SHA256")
                metricRep.sendUserData(name, surname, phoneNumber, UUID)
                dataStoreRepository.setPref(surname, savedSurname)
                dataStoreRepository.setPref(name, savedName)
                dataStoreRepository.setPref(phoneNumber, savedPhoneNumber)
                dataStoreRepository.setPref(isRememberMe, isRemembered)
                dataStoreRepository.setPref(UUID, uuid)
            }

    }

    fun getCity(context: Context, location: Location?) {
        if (location != null) {
            var address: List<Address>? = null
            try {
                val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
                address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val city = address!![0].locality.toString()
                viewModelScope.launch {
                    dataStoreRepository.setPref(city, savedCity)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }

    fun isValidPhone() : Boolean {
        val pattern = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}\$")
        return pattern.matcher(phoneNumber).matches()
//        return Patterns.PHONE.matcher(phoneNumber).matches()
    }

//    private fun askPermission() {
//        ActivityCompat.requestPermissions(MainActivity.context)
//    }
//    fun signIn(onSuccess: () -> Unit) {
//        if (isRememberMe) {
//            editPrefs()
//        }
//    }
}