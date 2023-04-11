package com.example.bookcourt.ui.profile

import android.net.Uri
import androidx.compose.runtime.*
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.user.UserRepositoryI
import com.example.bookcourt.models.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userRepositoryI: UserRepositoryI
):ViewModel(){

    init {
        val user: Flow<User> = flow{
            val data = getUser()
            emit(data)
        }
        viewModelScope.launch(Dispatchers.IO) {
            user.collect{
                name = it.name ?: ""
                email = it.email
                city = it.city
                bDayDAte = it.dayBD?:""
                sex = it.sex?:""
                profileImage = it.image?.toUri()
            }
        }

    }



    private suspend fun getUser(): User {
        val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)
        return userRepositoryI.loadData(userId.first())!!
    }

    var name  by mutableStateOf("")
    var email  by mutableStateOf("")
    var city  by mutableStateOf("")
    var bDayDAte by mutableStateOf("")
    var sex by mutableStateOf( "")
    var profileImage by mutableStateOf<Uri?>(null)

    fun onNameChanged(newText:String){
        name = newText
    }

    fun onEmailChanged(newText:String){
        email = newText
    }

    fun onCityChanged(newText:String){
        city = newText
    }

    fun onBDayDAteChanged(newText:String){
        bDayDAte = newText
    }

    fun onSexChanged(newText: String){
        sex = newText
    }

    fun onProfileImageChanged(newUri: Uri?){
        profileImage = newUri
    }


}