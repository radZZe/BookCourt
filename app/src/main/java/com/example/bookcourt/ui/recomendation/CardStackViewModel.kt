package com.example.bookcourt.ui.recomendation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.repositories.UserRepository
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardStackViewModel @Inject constructor(
    val dataStoreRepository: DataStoreRepository,
    private val metricRep: MetricsRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)

    //state flags
    var isEmpty by mutableStateOf(false)
    var isNotificationDisplay = dataStoreRepository.getBoolPref(DataStoreRepository.isNotificationDisplay)
    var isFirstNotification = mutableStateOf(false)
    var isBookInfoDisplay = mutableStateOf(false)

    var allBooks:List<MutableState<Book>> = listOf()
    var i by mutableStateOf(allBooks.size - 1)
    var counter by mutableStateOf(0)

    var currentItem = if(allBooks.isNotEmpty()) allBooks[i] else null


//    fun changeCurrentItem(){
//        if(i!=-1){
//            currentItem = allBooks[i]
//        }else{
//            isEmpty = true
//        }
//
//    }

    fun countEqualToLimit(){
        viewModelScope.launch(Dispatchers.IO) {
            isFirstNotification.value = true
            dataStoreRepository.setPref(true,DataStoreRepository.isNotificationDisplay)
        }

    }

    fun updateUserStatistic(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }
}