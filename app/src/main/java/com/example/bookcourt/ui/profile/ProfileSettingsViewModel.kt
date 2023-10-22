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
class ProfileSettingsViewModel @Inject constructor(
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
        nickname = user?.nickname ?: ""
        email = user?.email ?: ""
        surname = user?.surname ?: ""
        date = user?.dayBD ?: ""
        sex = user?.sex
        profileImage = user?.image?.toUri()

        readAmount = user?.readBooksList?.size ?: 0
        wantToRead = user?.wantToRead?.size ?: 0
    }

    private var userId by mutableStateOf("")
    var name by mutableStateOf("")
    var nickname by mutableStateOf("")
    var email by mutableStateOf("")
    var surname by mutableStateOf("")
    var date by mutableStateOf("")
    var sex by mutableStateOf<Sex?>(null)
    var profileImage by mutableStateOf<Uri?>(null)
    var isVisibleSnackBar by mutableStateOf(false)

    var readAmount by mutableStateOf(0)
    var liked by mutableStateOf(0)
    var wantToRead by mutableStateOf(0)

    fun onNameChanged(newText: String) {
        name = newText
    }

    fun onNicknameChanged(newText: String) {
        nickname = newText
    }

    fun onEmailChanged(newText: String) {
        email = newText
    }

    fun onSurnameChanged(newText: String) {
        surname = newText
    }

    fun onBDayDateChanged(newText: String) {
        date = newText
    }

    fun onSexChanged(newText: Sex) {
        sex = newText
    }

    fun onProfileImageChanged(newUri: Uri) {
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
            user?.let { it ->
                userRepositoryI.updateData(
                    User(
                        uid = userId,
                        nickname = nickname,
                        name = name.trim(),
                        email = email.trim(),
                        surname = surname.trim(),
                        image = profileImage?.toString(),
                        dayBD = date,
                        sex = sex,
                        readBooksList = it.readBooksList,
                        wantToRead = it.wantToRead,
                        liked = it.liked
                    )
                )
                showSnackBar()
            }
        }

    }


}