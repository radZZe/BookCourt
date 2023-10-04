package com.example.bookcourt.ui.statistics

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.PixelCopy
import android.view.View
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toAndroidRect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.data.room.user.UserRepositoryI
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

    private val _favAuthorsList = mutableStateMapOf<String, Int>()
    val favAuthorsList: Map<String, Int> = _favAuthorsList
    private val _favGenresList = mutableStateMapOf<String, Int>()
    val favGenresList: Map<String, Int> = _favGenresList

    var user = mutableStateOf<User?>(null)
    val readBooks = mutableStateOf<MutableList<Book>?>(null)
    val wantToRead = mutableStateOf<MutableList<Book>?>(null)
    private val isExitBtnVisible = mutableStateOf(true)
    private val isShareBtnVisible = mutableStateOf(true)
    private val isStoriesBarVisible = mutableStateOf(true)
    val isExitBtnHidden = mutableStateOf(false)
    val isShareBtnHidden = mutableStateOf(false)
    val isStoriesBarHidden = mutableStateOf(false)
    val composableBounds= mutableStateOf<Rect?>(null)
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

    fun getStoriesBarAlpha():Float{
        return if (isStoriesBarVisible.value){
            1f
        } else{
            0f
        }
    }

    fun getShareBtnAlpha():Float{
        return if (isShareBtnVisible.value){
            1f
        }
        else{
            0f
        }
    }

    fun getExitBtnAlpha():Float{
        return if (isExitBtnVisible.value){
            1f
        }
        else{
            0f
        }
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

    fun prepareForShare() {
        isExitBtnVisible.value=!isExitBtnVisible.value
        isShareBtnVisible.value=!isShareBtnVisible.value
        isStoriesBarVisible.value=!isStoriesBarVisible.value
    }

    fun shareStatistics(view:View, context:Context){
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            PixelCopy.request(
                (context as Activity).window,
                composableBounds.value?.let {
                    android.graphics.Rect(
                        it.left.toInt(),
                        it.top.toInt(),
                        it.right.toInt(),
                        it.bottom.toInt()
                    )
                },
                bitmap,
                { resultCode->
                    if (resultCode==PixelCopy.SUCCESS){
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
                ,Handler(Looper.getMainLooper())
            )
        }
        else{
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
        isShareBtnHidden.value=false
        isExitBtnHidden.value=false
        isStoriesBarHidden.value=false
        prepareForShare()
    }

}