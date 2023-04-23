package com.example.bookcourt.ui.profile

import android.net.Uri
import androidx.compose.runtime.*
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.room.user.UserRepositoryI
import com.example.bookcourt.models.user.Sex
import com.example.bookcourt.models.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userRepositoryI: UserRepositoryI
) : ViewModel() {

    private var user: User? = null


    suspend fun getUser() {
        val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)
        user = userRepositoryI.loadData(userId.first())!!
        initUserData()
    }

    private fun initUserData() {
        userId = user?.uid ?: ""
        name = user?.name ?: ""
        email = user?.email ?: ""
        city = user?.city ?: ""
        date = user?.dayBD ?: ""
        sex = user?.sex
        profileImage = user?.image?.toUri()
    }

    private var userId by mutableStateOf("")
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var city by mutableStateOf("")
    var date by mutableStateOf("")
    var sex by mutableStateOf<Sex?>(null)
    var profileImage by mutableStateOf<Uri?>(null)
    var isVisibleSnackBar by mutableStateOf(false)

    fun onNameChanged(newText: String) {
        name = newText
    }

    fun onEmailChanged(newText: String) {
        email = newText
    }

    fun onCityChanged(newText: String) {
        city = newText
    }

    fun onBDayDAteChanged(newText: String) {
        date = newText
    }

    fun onSexChanged(newText: Sex) {
        sex = newText
    }

    fun onProfileImageChanged(newUri: Uri?) {
        profileImage = newUri
    }

    fun showSnackBar(){
        viewModelScope.launch {
            isVisibleSnackBar = true
            delay(1000)
            isVisibleSnackBar = false
        }
    }

    fun saveUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            user?.let {
                userRepositoryI.updateData(
                    User(
                        uid = userId,
                        name = name,
                        email = email,
                        city = city,
                        image = profileImage as String?,
                        dayBD = date,
                        sex = sex,
                        readBooksList = it.readBooksList,
                        wantToRead = it.wantToRead
                    )
                )
            }
        }

    }


}