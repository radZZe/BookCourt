package com.example.bookcourt.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor():ViewModel(){

    var name  by mutableStateOf<String?>(null)
    var email  by mutableStateOf<String?>(null)
    var city  by mutableStateOf<String?>(null)
    var bDayDAte by mutableStateOf<String?>(null)

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
}