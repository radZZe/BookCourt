package com.example.bookcourt.ui.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor():ViewModel(){

    var name  by mutableStateOf<String>("")
    var email  by mutableStateOf<String>("")
    var city  by mutableStateOf<String?>(null)
    var bDayDAte by mutableStateOf<String?>(null)
    var sex by mutableStateOf<String?>(null)
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