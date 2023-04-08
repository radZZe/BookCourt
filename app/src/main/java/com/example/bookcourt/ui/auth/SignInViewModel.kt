package com.example.bookcourt.ui.auth

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.text.TextUtils
import androidx.compose.runtime.getValue
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
import com.example.bookcourt.data.user.UserRepositoryI
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.models.user.User
import com.example.bookcourt.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val metricRep: MetricsRepository,
    private val hashing: Hashing,
    private val userRepositoryI: UserRepositoryI
) : ViewModel() {

    var email by mutableStateOf("")
    var name by mutableStateOf("")
    var surname by mutableStateOf("")
    var phoneNumber by mutableStateOf("+7")
    var isRememberMe by mutableStateOf(false)
    var city by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var isTutorChecked = dataStoreRepository.getBoolPref(DataStoreRepository.isTutorChecked)

    var sessionTime = System.currentTimeMillis().toInt()

    fun onEmailChanged(newText: String){
        email = newText
    }

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

    suspend fun sendUserMetric(context: Context,name:String,surname:String,phone:String,city:String,uuid:String){
            metricRep.sendUserData(name,surname,phone,city,uuid,context)
    }

    private suspend fun editPrefs(UUID: String) {
            dataStoreRepository.setPref(isRememberMe, isRemembered)
            dataStoreRepository.setPref(city, savedCity)
            dataStoreRepository.setPref(UUID, uuid)
    }

    private suspend fun sendMetrics() {
        sessionTime = System.currentTimeMillis().toInt() - sessionTime
        metricRep.appTime(sessionTime, MetricType.SCREEN_SESSION_TIME,"Sign in")
        metricRep.onClick(DataClickMetric(Buttons.SIGN_IN, Screens.SignIn.route))
    }

    fun saveUser(onNavigateToCategorySelection: () -> Unit,
                 onNavigateToTutorial: () -> Unit, context:Context) {
        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            val UUID = hashing.getHash("AB$name$phoneNumber".toByteArray(), "SHA256")
            val user = User(
                uid = UUID,
                email = email,
                city = city,
                readBooksList = mutableListOf(),
                wantToRead = mutableListOf()
            )
            editPrefs(UUID)
            sendMetrics()
            userRepositoryI.saveData(user)
            sendUserMetric(context,name,surname,phoneNumber,city,UUID)
            withContext(Dispatchers.Main){
                if (isTutorChecked.first()) {
                    onNavigateToCategorySelection()
                } else {
                    onNavigateToTutorial()
                }
            }
        }
    }

    fun isValidEmail(email:String):Boolean{
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(): Boolean {
        val pattern =
            Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}\$")
        return pattern.matcher(phoneNumber).matches()
    }
}