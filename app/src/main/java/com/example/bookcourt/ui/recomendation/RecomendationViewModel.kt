package com.example.bookcourt.ui.recomendation

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.uuid
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.data.room.UserRepository
import com.example.bookcourt.models.Book
import com.example.bookcourt.models.BookRemote
import com.example.bookcourt.models.User
import com.example.bookcourt.models.UserAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class RecomendationViewModel @Inject constructor(
    val repository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val metricRep: MetricsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    //    var allBooks = mutableStateOf<MutableList<Book>?>(null)
    var dataIsReady by mutableStateOf(false)
//    private var _allBooks = mutableStateListOf<Book>()
    private var _validBooks = mutableStateListOf<Book>()
    val validBooks:List<Book> = _validBooks
//    var readBooks = dataStoreRepository.getPref(readBooksList)
//    val isEmpty = mutableStateOf(false)
//    val tutorState = dataStoreRepository.getBoolPref(isTutorChecked)
//    var isScreenChanged = false //bullshit
    var isFirstDataLoading by mutableStateOf(true)
    var stateNotificationDisplay = false;

//    val tutorState = dataStoreRepository.getBoolPref(isTutorChecked) // dead feature

    fun getAllBooks(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val jobUserId = async { dataStoreRepository.getPref(DataStoreRepository.uuid) }
            val userId = jobUserId.await()
            jobUserId.cancel()
            val jobUserIdValue = async{userId.first()}
            val id = jobUserIdValue.await()
            jobUserIdValue.cancel()
            var test = 5;
            val jobUser = async { userRepository.getUserById(id) }
            val user = jobUser.await()
            jobUser.cancel()

            val readBooks = user.readBooksList

            val job = async { repository.getAllBooks(context)!! }
            val json = job.await()
            job.cancel()
            val data = Json.decodeFromString<MutableList<BookRemote>>("""$json""")
            val allBooksItems = data.map {
                it.toBook()
            }

            if (readBooks.isEmpty()) {
                _validBooks.addAll(allBooksItems)
            } else {
                var items = allBooksItems.filter {
                    it !in readBooks
                }
                _validBooks.addAll(items)
            }
            isFirstDataLoading = false
            dataIsReady = true
        }
    }

//    fun addElementToAllBooks(element: Book) {
//        _allBooks.add(element)
//    }
//
//    fun deleteElementFromAllBooks(element: Book) {
//        _allBooks.remove(element)
//    }

    fun metricSwipeLeft(book: Book) {
        var userAction =
            UserAction(action = "Не понравилась книга { название:${book.bookInfo.title} автор: ${book.bookInfo.author}} ") // добавить сюда id книги
        viewModelScope.launch {
            metricRep.onAction(userAction)
        }
    }

    fun metricSwipeRight(book: Book) {
        var userAction =
            UserAction(action = "Понравилась книга { название:${book.bookInfo.title} автор: ${book.bookInfo.author}} ") // добавить сюда id книги
        viewModelScope.launch {
            metricRep.onAction(userAction)
        }
    }

    fun metricSwipeTop(book: Book) {
        var userAction =
            UserAction(action = "Добавил в хочу прочесть книгу { название:${book.bookInfo.title} автор: ${book.bookInfo.author}} ") // добавить сюда id книги
        viewModelScope.launch {
            metricRep.onAction(userAction)
        }
    }

    fun metricSwipeDown(book: Book) {
        var userAction =
            UserAction(action = "Пропустил книгу { название:${book.bookInfo.title} автор: ${book.bookInfo.author}} ") // добавить сюда id книги
        viewModelScope.launch {
            metricRep.onAction(userAction)
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