package com.example.bookcourt.ui.recomendation

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.isTutorChecked
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.readBooksList
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.data.room.UserRepository
import com.example.bookcourt.models.Book
import com.example.bookcourt.models.BookRemote
import com.example.bookcourt.models.User
import com.example.bookcourt.models.UserAction
import com.example.bookcourt.utils.Converters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class RecomendationViewModel @Inject constructor(
    val repository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val metricRep:MetricsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var allBooks = mutableStateListOf<Book>()
    var validBooks = mutableStateListOf<Book>()
    val isEmpty = mutableStateOf(false)
    var isScreenChanged = false //bullshit

//    private val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)

//    val tutorState = dataStoreRepository.getBoolPref(isTutorChecked) // dead feature

    @RequiresApi(Build.VERSION_CODES.O)
    fun metricSwipeLeft(book:Book){
        var userAction = UserAction(action = "Не понравилась книга { название:${book.bookInfo.title} автор: ${book.bookInfo.author}} ") // добавить сюда id книги
        viewModelScope.launch {
            metricRep.onAction(userAction)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun metricSwipeRight(book:Book){
        var userAction = UserAction(action = "Понравилась книга { название:${book.bookInfo.title} автор: ${book.bookInfo.author}} ") // добавить сюда id книги
        viewModelScope.launch {
            metricRep.onAction(userAction)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun metricSwipeTop(book: Book){
        var userAction = UserAction(action = "Добавил в хочу прочесть книгу { название:${book.bookInfo.title} автор: ${book.bookInfo.author}} ") // добавить сюда id книги
        viewModelScope.launch {
            metricRep.onAction(userAction)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun metricSwipeDown(book: Book){
        var userAction = UserAction(action = "Пропустил книгу { название:${book.bookInfo.title} автор: ${book.bookInfo.author}} ") // добавить сюда id книги
        viewModelScope.launch {
            metricRep.onAction(userAction)
        }
    }

    fun getAllBooks(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {

            val jobUserId = async { dataStoreRepository.getPref(DataStoreRepository.uuid) }
            val userId = jobUserId.await()

            val jobUser = async { userRepository.getUserById(userId.first()) }
            val user = jobUser.await()
            val readBooks = user.readBooksList

            val job = async { repository.getAllBooks(context)!! }
            val json = job.await()
            val data = Json.decodeFromString<MutableList<BookRemote>>("""$json""")
            val allBooksItems = data.map {
                it.toBook()
            }
            allBooks.addAll(allBooksItems)

            if (readBooks.isEmpty()) {
                validBooks = allBooks
            } else {
                for (book in allBooks!!) {
                    if (book !in readBooks) {
                        validBooks!!.add(book)
                    }
                }
            }
        }
    }



//    private var tutorStateBool by mutableStateOf(false)

//    fun editTutorState() {
//        tutorStateBool = true
//        viewModelScope.launch {
//            dataStoreRepository.setPref(tutorStateBool, isTutorChecked)
//        }
//    }

}