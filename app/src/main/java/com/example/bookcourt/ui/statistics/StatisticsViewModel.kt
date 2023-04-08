package com.example.bookcourt.ui.statistics

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.data.user.UserRepositoryI
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.models.user.User
import com.example.bookcourt.utils.BitmapUtils
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
        val topAuthorsMap = mutableMapOf<String, Int>()
        for (book in user.value!!.readBooksList) {
            if (topAuthorsMap.containsKey(book.bookInfo.author)) {
                var count = topAuthorsMap[book.bookInfo.author]!!
                topAuthorsMap[book.bookInfo.author] = (count + 1)
            } else {
                topAuthorsMap[book.bookInfo.author] = 1
            }
        }
        return topAuthorsMap.toList().sortedByDescending { (_, value) -> value }.toMap()
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

    fun shareStatistics(
        view:View,
        context: Context
    ){
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.layout(
            view.left,
            view.top,
            view.right,
            view.bottom
        )
        view.draw(canvas)

        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_STREAM,
                BitmapUtils.getBitmapUri(
                    context,
                    bitmap,
                    "statistics",
                    "images/"
                )
            )
            type = "image/jpeg"
        }
        context.startActivity(Intent.createChooser(shareIntent, null))
    }

}