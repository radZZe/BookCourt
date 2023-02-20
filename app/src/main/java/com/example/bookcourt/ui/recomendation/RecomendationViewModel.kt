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
import com.example.bookcourt.models.ClickMetric
import com.example.bookcourt.models.User
import com.example.bookcourt.utils.MetricType
import com.example.bookcourt.utils.MetricType.SKIP_BOOK
import com.example.bookcourt.utils.MetricType.DISLIKE_BOOK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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
    var dataIsReady by mutableStateOf(false)
    val validBooks:List<Book> = _validBooks
    var isFirstDataLoading by mutableStateOf(true)

    fun getAllBooks(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = dataStoreRepository.getPref(uuid)
            user = userRepository.getUserById(userId.first())

            val readBooks = user.readBooksList.map { it.bookInfo }

            val job = async { networkRepository.getAllBooks(context)!! }
            val json = job.await()
            job.cancel()

            val data = Json.decodeFromString<MutableList<BookRemote>>("""$json""")
            val allBooksItems = data.map { it.toBook() }

            if (readBooks.isEmpty()) {
                _validBooks.addAll(allBooksItems)
            } else {
                var items = allBooksItems.filter { book ->
                    book.bookInfo !in readBooks
                }
                _validBooks.addAll(items)
            }
            isFirstDataLoading = false
            dataIsReady = true
        }
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
    
    fun metricClick(clickMetric: ClickMetric) {
        viewModelScope.launch(Dispatchers.IO) {
            metricRep.onClick(clickMetric)
        }
    }

}