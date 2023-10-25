package com.example.bookcourt.ui.auth

import android.content.Context
import android.text.TextUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.isAuthenticated
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.uuid
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.room.user.UserRepositoryI
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.models.user.User
import com.example.bookcourt.utils.Buttons
import com.example.bookcourt.utils.Hashing
import com.example.bookcourt.utils.MetricType
import com.example.bookcourt.utils.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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
    var city by mutableStateOf("")

    private var sessionTime = System.currentTimeMillis().toInt()

    fun onEmailChanged(newText: String) {
        email = newText
    }

    private suspend fun sendUserMetric(
        context: Context,
        name: String,
        surname: String,
        phone: String,
        city: String,
        uuid: String
    ) {
        metricRep.sendUserData(name, surname, phone, city, uuid, context)
    }

    private suspend fun editPrefs(UUID: String) {
        dataStoreRepository.setPref(UUID, uuid)
    }

    private suspend fun sendMetrics() {
        sessionTime = System.currentTimeMillis().toInt() - sessionTime
        metricRep.appTime(sessionTime, MetricType.SCREEN_SESSION_TIME, "Sign in")
        metricRep.onClick(DataClickMetric(Buttons.SIGN_IN, Screens.SignIn.route))
    }

    fun saveUser(
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (dataStoreRepository.getBoolPref(isAuthenticated).first()) {
                val userId = dataStoreRepository.getPref(uuid)
                val user = userRepositoryI.loadData(userId.first())!!
                user.email = email
                userRepositoryI.updateData(user)
            } else {
                val UUID = hashing.getHash("AB$name$phoneNumber".toByteArray(), "SHA256")
                val user = User(
                    uid = UUID,
                    email = email,
                    surname = city,
                    readBooksList = mutableListOf(),
                    wantToRead = mutableListOf(),
                    liked = mutableListOf()
                )
                userRepositoryI.saveData(user)
                sendUserMetric(context, name, surname, phoneNumber, city, UUID)
                editPrefs(UUID)
            }
            sendMetrics()
        }
    }

    fun isValidEmail(): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

}