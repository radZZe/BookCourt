package com.example.bookcourt.ui.recomendation

import android.content.Context
import android.util.Log
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
import com.example.bookcourt.data.repositories.UserRepository
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.BookRemote
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.models.user.User
import com.example.bookcourt.utils.MetricType
import com.example.bookcourt.utils.MetricType.SKIP_BOOK
import com.example.bookcourt.utils.MetricType.DISLIKE_BOOK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RecomendationViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val metricRep: MetricsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    lateinit var user : User

    private var _validBooks = mutableStateListOf<Book>()
    val validBooks:MutableList<Book> = _validBooks
    var dataIsReady by mutableStateOf(false)
    var isFirstDataLoading by mutableStateOf(true)
    private var sessionTime = System.currentTimeMillis().toInt()
    private var fetchJob: Job? = null

    fun getAllBooks(context: Context) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(Dispatchers.IO) {
            try{
                user = getUser()
                val allBooksItems = convertBooksJsonToList(context)
                booksValidation(user,allBooksItems)
                isFirstDataLoading = false
                dataIsReady = true
            }catch (ioe:IOException){
                Log.d("getAllBooks","error cause ${ioe.cause}")
            }

        }
    }

    fun booksValidation(user: User, allBooks:List<Book>){
        val readBooks = user.readBooksList.map { it.bookInfo }
        if (readBooks.isEmpty()) {
            _validBooks.addAll(allBooks)
        } else {
            var items = allBooks.filter { book ->
                book.bookInfo !in readBooks
            }
            _validBooks.addAll(items)
        }
    }

    suspend fun convertBooksJsonToList(context: Context):List<Book>{
        val json = networkRepository.getAllBooks(context)!!
        val data = Json.decodeFromString<MutableList<BookRemote>>("""$json""")
        val allBooksItems = data.map { it.toBook() }
        return allBooksItems
    }

    suspend fun getUser(): User {
        val userId = dataStoreRepository.getPref(uuid)
        user = userRepository.getUserById(userId.first())
        return user
    }


    fun metricSwipeLeft(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            metricRep.onSwipe(book,DISLIKE_BOOK)
        }
    }

    fun metricSwipeRight(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            metricRep.onSwipe(book, MetricType.LIKE_BOOK)
        }
    }

    fun metricSwipeTop(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            metricRep.onSwipe(book, MetricType.WANT_TO_READ_BOOK)
        }
    }

    fun metricSwipeDown(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            metricRep.onSwipe(book, SKIP_BOOK)
        }
    }
    
    fun metricClick(clickMetric: DataClickMetric) {
        viewModelScope.launch(Dispatchers.IO) {
            metricRep.onClick(clickMetric)
        }
    }

    fun metricScreenTime() {
        viewModelScope.launch(Dispatchers.IO) {
            sessionTime = System.currentTimeMillis().toInt() - sessionTime
            metricRep.appTime(sessionTime, MetricType.SCREEN_SESSION_TIME,"Recomendation")
            sessionTime = System.currentTimeMillis().toInt()
        }
    }

}