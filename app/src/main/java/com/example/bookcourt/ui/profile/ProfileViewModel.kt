package com.example.bookcourt.ui.profile

import android.net.Uri
import androidx.compose.runtime.*
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.user.UserRepositoryI
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.user.Sex
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

    val user:Flow<User> = flow{
        val data = getUser()
        emit(data)
    }.shareIn(
        scope = viewModelScope,
        replay = 1,
        started = SharingStarted.WhileSubscribed(),
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            user.collect{
                userId = it.uid
                name = it.name ?: ""
                email = it.email
                city = it.city
                date = it.dayBD ?:""
                sex = it.sex
                profileImage = it.image?.toUri()
                readBooksList = it.readBooksList
                wantToRead = it.wantToRead
            }
        }

    }



    private suspend fun getUser(): User {
        val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)
        return userRepositoryI.loadData(userId.first())!!
    }
    var readBooksList = mutableListOf<Book>()
    private var wantToRead = mutableListOf<Book>()
    private var userId by mutableStateOf("")
    var name  by mutableStateOf("")
    var email  by mutableStateOf("")
    var city  by mutableStateOf("")
    var date by mutableStateOf("")
    var sex by mutableStateOf<Sex?>( null)
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
        date = newText
    }

    fun onSexChanged(newText: Sex){
        sex = newText
    }

    fun onProfileImageChanged(newUri: Uri?){
        profileImage = newUri
    }

    fun saveUserData(){
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(
                uid = userId,
                name = name,
                email = email,
                city = city,
                image = profileImage.toString(),
                dayBD = date,
                sex = sex,
                readBooksList = readBooksList,
                wantToRead = wantToRead
            )
            userRepositoryI.updateData(user)
        }

    }


}