package com.example.bookcourt.ui.recomendation

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.*
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

    lateinit var user: User

    var validBooks = mutableStateListOf<Book>()
    var dataIsReady by mutableStateOf(false)

    var isFirstDataLoading by mutableStateOf(true)
    var isNotificationDisplay = dataStoreRepository.getBoolPref(DataStoreRepository.isNotificationDisplay)
    var isFirstNotification = mutableStateOf(false)

    private var sessionTime = System.currentTimeMillis().toInt()
    private var fetchJob: Job? = null

    val limitSwipeValue = 3;
    var counter by mutableStateOf(0)


    var blurValueRecommendationScreen = Animatable(0f)

    fun displayNotificationMessage(){
        viewModelScope.launch {
            blurValueRecommendationScreen.snapTo(20f)
        }
    }

    fun closeNotificationMessage(){
        viewModelScope.launch {
            blurValueRecommendationScreen.snapTo(0f)
        }
    }

    fun getAllBooks(context: Context) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                user = getUser()
                val allBooksItems = convertBooksJsonToList(context)
                booksValidation(user, allBooksItems)
                isFirstDataLoading = false
                dataIsReady = true
            } catch (ioe: IOException) {
                Log.d("getAllBooks", "error cause ${ioe.cause}")
            }

        }
    }

    fun booksValidation(user: User, allBooks: List<Book>) {
        val readBooks = user.readBooksList.map { it.bookInfo }
        if (readBooks.isEmpty()) {
            validBooks.addAll(allBooks)
        } else {
            var items = allBooks.filter { book ->
                book.bookInfo !in readBooks
            }
            validBooks.addAll(items)
        }
    }

    suspend fun convertBooksJsonToList(context: Context): List<Book> {
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
            metricRep.onSwipe(book, DISLIKE_BOOK)
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
            metricRep.appTime(sessionTime, MetricType.SCREEN_SESSION_TIME, "Recomendation")
            sessionTime = System.currentTimeMillis().toInt()
        }
    }

    fun countEqualToLimit(){
        viewModelScope.launch(Dispatchers.IO) {
            isFirstNotification.value = true
            dataStoreRepository.setPref(true,DataStoreRepository.isNotificationDisplay)
        }

    }

}