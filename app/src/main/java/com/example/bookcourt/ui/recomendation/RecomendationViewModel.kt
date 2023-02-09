package com.example.bookcourt.ui.recomendation

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.isTutorChecked
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.readBooksList
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.models.Book
import com.example.bookcourt.models.BookRemote
import com.example.bookcourt.models.UserAction
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
    private val metricRep:MetricsRepository
) : ViewModel() {

    var allBooks = mutableStateOf<MutableList<Book>?>(null)
    var readBooks = dataStoreRepository.getPref(readBooksList)
    val isEmpty = mutableStateOf(false)
    val tutorState = dataStoreRepository.getBoolPref(isTutorChecked)
    var isScreenChanged = false //bullshit

    @RequiresApi(Build.VERSION_CODES.O)
    fun metricSwipeLeft(book:Book){
        var userAction = UserAction(action = "Не понравилась книга { название:${book.name} автор: ${book.author}} ") // добавить сюда id книги
        viewModelScope.launch {
            metricRep.onAction(userAction)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun metricSwipeRight(book:Book){
        var userAction = UserAction(action = "Понравилась книга { название:${book.name} автор: ${book.author}} ") // добавить сюда id книги
        viewModelScope.launch {
            metricRep.onAction(userAction)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun metricSwipeTop(book: Book){
        var userAction = UserAction(action = "Добавил в хочу прочесть книгу { название:${book.name} автор: ${book.author}} ") // добавить сюда id книги
        viewModelScope.launch {
            metricRep.onAction(userAction)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun metricSwipeDown(book: Book){
        var userAction = UserAction(action = "Пропустил книгу { название:${book.name} автор: ${book.author}} ") // добавить сюда id книги
        viewModelScope.launch {
            metricRep.onAction(userAction)
        }
    }

    fun getAllBooks(context: Context) {
        val jobMain = viewModelScope.launch(Dispatchers.IO) {
            val job = async { repository.getAllBooks(context)!! }
            val json = job.await()
            val data = Json.decodeFromString<MutableList<BookRemote>>("""$json""")
            allBooks.value = data.map {
                it.toBook()
            } as MutableList<Book>
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