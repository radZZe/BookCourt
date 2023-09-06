package com.example.bookcourt.ui.statistics

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.data.room.user.UserRepositoryI
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.models.user.User
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
    private val userRepositoryI: UserRepositoryI,
    val dataStoreRepository: DataStoreRepository,
    private val metricsRepository: MetricsRepository
) : ViewModel() {
    private val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)

    private val _favAuthorsList = mutableStateMapOf<String, Int>()
    val favAuthorsList: Map<String, Int> = _favAuthorsList
    private val _favGenresList = mutableStateMapOf<String, Int>()
    val favGenresList: Map<String, Int> = _favGenresList

    var user = mutableStateOf<User?>(null)
    val readBooks = mutableStateOf<MutableList<Book>?>(null)
    val wantToRead = mutableStateOf<MutableList<Book>?>(null)
    private var sessionTime = System.currentTimeMillis().toInt()

    fun getUserStats() {
        val job = viewModelScope.launch(Dispatchers.IO) {
                user = mutableStateOf(userRepositoryI.loadData(userId.first()))
                user.value?.let { getReadBooksList(it) }
                user.value?.let { getWantedBooksList(it) }
        }
        runBlocking {
            job.join()
            user.value?.let {
                getTopGenres()
                getTopAuthors()
            }
        }
    }

    private fun getReadBooksList(user: User) {
        readBooks.value =  user.readBooksList  as MutableList<Book>?
    }

    private  fun getWantedBooksList(user: User) {
        wantToRead.value = user.wantToRead as MutableList<Book>?
    }

    private fun getTopGenres() {
        val topGenreMap = mutableMapOf<String, Int>()
        if (user.value?.readBooksList != null) {
            for (book in user.value!!.readBooksList) {
                if (topGenreMap.containsKey(book.bookInfo.genre)) {
                    val count = topGenreMap[book.bookInfo.genre]!!
                    topGenreMap[book.bookInfo.genre] = (count + 1)
                } else {
                    topGenreMap[book.bookInfo.genre] = 1
                }
            }
        }
        _favGenresList.plusAssign(topGenreMap.toList().sortedByDescending { (_, value) -> value }.toMap())
    }

    private fun getTopAuthors() {
        val topAuthorsMap = mutableMapOf<String, Int>()
        if (user.value?.readBooksList != null) {
            for (book in user.value!!.readBooksList) {
                if (topAuthorsMap.containsKey(book.bookInfo.author)) {
                    val count = topAuthorsMap[book.bookInfo.author]!!
                    topAuthorsMap[book.bookInfo.author] = (count + 1)
                } else {
                    topAuthorsMap[book.bookInfo.author] = 1
                }
            }
        }
        _favAuthorsList.plusAssign(topAuthorsMap.toList().sortedByDescending { (_, value) -> value }.toMap())
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

    fun shareStatistics(): String {
        val topAuthors = favAuthorsList.toList()
        val topGenres = favGenresList.toList()

        val topAuthorsText = "${topAuthors.getOrNull(0)?.first ?: ""}\n" +
                "${topAuthors.getOrNull(1)?.first ?: ""}\n" +
                "${topAuthors.getOrNull(2)?.first ?: ""}\n"
        val topGenresText = "${topGenres.getOrNull(0)?.first ?: ""}\n" +
                "${topGenres.getOrNull(1)?.first ?: ""}\n" +
                "${topGenres.getOrNull(2)?.first ?: ""}\n"

        return "Пользователь ${user.value?.name ?: ""} прочитал ${readBooks.value?.size} книг\n\n" +
                "Любимые авторы: $topAuthorsText \n" + "Любые жанры: $topGenresText"
    }

}