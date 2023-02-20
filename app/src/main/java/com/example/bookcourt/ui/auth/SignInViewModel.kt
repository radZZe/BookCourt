package com.example.bookcourt.ui.auth

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.isRemembered
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedCity
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.uuid
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.room.UserRepository
import com.example.bookcourt.models.ClickMetric
import com.example.bookcourt.models.User
import com.example.bookcourt.models.UserStatistics
import com.example.bookcourt.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val metricRep: MetricsRepository,
    private val hashing: Hashing,
    private val userRepository: UserRepository
) : ViewModel() {

    var name by mutableStateOf("")
    var surname by mutableStateOf("")
    var phoneNumber by mutableStateOf("+7")
    var isRememberMe by mutableStateOf(false)
    var city by mutableStateOf("")

    var sessionTime = System.currentTimeMillis().toInt()

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

    fun onCityChanged(newText: String) {
        city = newText
    }

    private suspend fun editPrefs(UUID: String) {
            dataStoreRepository.setPref(isRememberMe, isRemembered)
            dataStoreRepository.setPref(city, savedCity)
            dataStoreRepository.setPref(UUID, uuid)
    }

    private suspend fun sendMetrics() {
        sessionTime = System.currentTimeMillis().toInt() - sessionTime
        metricRep.appTime(sessionTime, MetricType.SCREEN_SESSION_TIME)
        metricRep.onClick(ClickMetric(Buttons.SIGN_IN, Screens.SignIn.route))
    }

    fun saveUser(navController:NavController) {
        viewModelScope.launch(Dispatchers.IO) {
            val UUID = hashing.getHash("AB$name$phoneNumber".toByteArray(), "SHA256")
            val user = User(
                uid = UUID,
                name = name,
                surname = surname,
                phone = phoneNumber,
                city = city,
                readBooksList = mutableListOf(),
                wantToRead = mutableListOf()
            )
            editPrefs(UUID)
            sendMetrics()
            val job = async { userRepository.addUser(user) }
            job.await()
            withContext(Dispatchers.Main){
                navController.popBackStack()
                navController.navigate(route = Screens.Recommendation.route)
            }
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

    fun isValidPhone(): Boolean {
        val pattern =
            Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}\$")
        return pattern.matcher(phoneNumber).matches()
    }
}