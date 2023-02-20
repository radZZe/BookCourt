package com.example.bookcourt.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.room.UserRepository
import com.example.bookcourt.models.Book
import com.example.bookcourt.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
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
    var isNotificationDisplay = mutableStateOf(false)
    var isBookInfoDisplay = mutableStateOf(false)

    var allBooks:List<MutableState<Book>> = listOf()
    var i by mutableStateOf(allBooks.size - 1)
    var direction = mutableStateOf<String?>(null)
    var counter by mutableStateOf(0)

    var currentItem = if(allBooks.isNotEmpty()) allBooks[i] else null //КОСТЫЛЬ - че орешь?
//    var readBooks = mutableListOf<Book>()
//    var wantToRead = mutableListOf<Book>()


    fun changeCurrentItem(){
        if(i!=-1){
            currentItem = allBooks[i]
        }else{
            isEmpty = true
        }

    }

    fun countEqualToLimit(){
        isNotificationDisplay.value = !isNotificationDisplay.value
    }

    

//    fun getReadBooks() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val job = async { userRepository.getUserById(userId.first()) }
//            currentUser = user
//            var user = job.await()
//            readBooks = user.readBooksList as MutableList<Book>
//            wantToRead = user.wantToRead as MutableList<Book>
//        }
//    }

    fun updateUserStatistic(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
//            val currentUser = userRepository.getUserById(userId.first())
//            val updatedUser = updateUserLists(currentUser, readBooks, wantToRead)
//            userRepository.updateUser(updatedUser)
            userRepository.updateUser(user)
        }
    }

//    private suspend fun updateUserLists(
//        user: User,
//        readBooksList: List<Book>,
//        wantToReadList: List<Book>
//    ): User {
//        user.readBooksList.addAll(readBooksList)
//        user.wantToRead.addAll(wantToReadList)
//        readBooks.clear()
//        wantToRead.clear()
//        return user
//    }

    fun changeCurrentItem(item: Book) {
        currentItem?.value = item
    }

    fun changeDirection(newDirection:String?,item: MutableState<Book>?){
        if(item != null){
            item.value.onSwipeDirection = newDirection
        }
    }
}