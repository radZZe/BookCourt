package com.example.bookcourt.ui.profile

import android.net.Uri
import androidx.compose.runtime.*
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.room.user.UserRepositoryI
import com.example.bookcourt.models.user.Sex
import com.example.bookcourt.models.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userRepositoryI: UserRepositoryI
) : ViewModel() {

    private var user: User? = null
    val isAuthenticated = dataStoreRepository.getBoolPref(DataStoreRepository.isAuthenticated)


    suspend fun getUser() {
        val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)
        user = userRepositoryI.loadData(userId.first())!!
        initUserData()
    }

    private fun initUserData() {
        userId = user?.uid ?: ""
        name = user?.name ?: ""
        email = user?.email ?: ""
        surname = user?.surname ?: ""
        date = user?.dayBD ?: ""
        sex = user?.sex
        profileImage = user?.image?.toUri()
        nickname = user?.nickname ?: ""
        readAmount = user?.readBooksList?.size ?: 0
        wantToRead = user?.wantToRead?.size ?: 0
        liked = user?.liked?.size ?: 0
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

    suspend fun editPrefs() {
        dataStoreRepository.setPref(true, DataStoreRepository.isAuthenticated)
    }
}