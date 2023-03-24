package com.example.bookcourt.ui.statistics

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.data.repositories.UserRepository
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.models.user.User
import com.example.bookcourt.ui.statistics.StatisticsScreenRequest.AMOUNT_OF_BOOKS
import com.example.bookcourt.utils.MetricType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val networkRepository: NetworkRepository,
    private val userRepository: UserRepository,
    val dataStoreRepository: DataStoreRepository,
    private val metricsRepository: MetricsRepository
) : ViewModel() {
    private val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)

    var user = mutableStateOf<User?>(null)
    val currentScreen = mutableStateOf<String>(AMOUNT_OF_BOOKS)
    val readBooks = mutableStateOf<MutableList<Book>?>(null)
    val wantToRead = mutableStateOf<MutableList<Book>?>(null)
    private var sessionTime = System.currentTimeMillis().toInt()

    fun getUserStats() {
        val job = viewModelScope.launch(Dispatchers.IO) {
                user = mutableStateOf(userRepository.getUserById(userId.first()))
                user.value?.let { getReadBooksList(it) }
                user.value?.let { getWantedBooksList(it) }
        }
        runBlocking {
            job.join()
            user.value?.let { getTopGenres() }
        }
    }

    private fun getReadBooksList(user: User) {
        readBooks.value =  user.readBooksList  as MutableList<Book>?
    }

    private  fun getWantedBooksList(user: User) {
        wantToRead.value = user.wantToRead as MutableList<Book>?
    }

    fun getTopGenres(): Map<String, Int> {
        val topGenreMap = mutableMapOf<String, Int>()
        for (book in user.value!!.readBooksList) {
            if (topGenreMap.containsKey(book.bookInfo.genre)) {
                var count = topGenreMap[book.bookInfo.genre]!!
                topGenreMap[book.bookInfo.genre] = (count + 1)
            } else {
                topGenreMap[book.bookInfo.genre] = 1
            }
        }
        return topGenreMap.toList().sortedByDescending { (_, value) -> value }.toMap()
    }

    fun getTopAuthors(): Map<String, Int> {
        val topGenreMap = mutableMapOf<String, Int>()
        for (book in user.value!!.readBooksList) {
            if (topGenreMap.containsKey(book.bookInfo.author)) {
                var count = topGenreMap[book.bookInfo.author]!!
                topGenreMap[book.bookInfo.author] = (count + 1)
            } else {
                topGenreMap[book.bookInfo.author] = 1
            }
        }
        return topGenreMap.toList().sortedByDescending { (_, value) -> value }.toMap()
    }

    fun sendOnClickMetric(clickMetric: DataClickMetric) {
        viewModelScope.launch(Dispatchers.IO) {
            metricsRepository.onClick(clickMetric)
        }
        Log.d("Screen", "cross clicked")
    }

    fun metricScreenTime() {
        viewModelScope.launch(Dispatchers.IO) {
            sessionTime = System.currentTimeMillis().toInt() - sessionTime
            metricsRepository.appTime(sessionTime, MetricType.SCREEN_SESSION_TIME,"Statistics")
        }
        Log.d("Screen", "metric worked")
    }

}