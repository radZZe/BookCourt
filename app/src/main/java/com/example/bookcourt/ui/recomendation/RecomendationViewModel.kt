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
    private var _allBooks = mutableStateListOf<Book>()
    private var _validBooks = mutableStateListOf<Book>()
    val validBooks: List<Book> = _validBooks
    val allBooks: List<Book> = _allBooks
    lateinit var data: MutableList<BookRemote>
    lateinit var user: User

    //    var readBooks = dataStoreRepository.getPref(readBooksList)
//    val isEmpty = mutableStateOf(false)
//    val tutorState = dataStoreRepository.getBoolPref(isTutorChecked)
//    var isScreenChanged = false //bullshit
    var isFirstDataLoading by mutableStateOf(true)
    var stateNotificationDisplay = false;

//    val tutorState = dataStoreRepository.getBoolPref(isTutorChecked) // dead feature

    fun getAllBooks(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
//            val uid = async { dataStoreRepository.getPref(uuid).first() }
            val uid = dataStoreRepository.getString34(uuid)
            val user = async { userRepository.getUserById(uid!!) }
            val allBooks = repository.getAllBooks(context)
            data = Json.decodeFromString<MutableList<BookRemote>>("""$allBooks""")


            val readBooks = user.await().readBooksList
            val allBooksItems = data.map {
                it.toBook()
            }

            _allBooks.addAll(allBooksItems)

            if (readBooks.isEmpty()) {
                _validBooks.addAll(_allBooks)
            } else {
                var items = _allBooks.filter {
                    it !in readBooks
                }
                _validBooks.addAll(items)
            }
            dataIsReady = true
        }
    }

    fun addElementToAllBooks(element: Book) {
        _allBooks.add(element)
    }

    fun deleteElementFromAllBooks(element: Book) {
        _allBooks.remove(element)
    }

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