package com.example.bookcourt.ui.recommendation

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.api.BooksApi
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.uuid
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.data.room.basket.BasketRepositoryI
import com.example.bookcourt.data.room.user.UserRepositoryI
import com.example.bookcourt.models.BookData
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.BookDto
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.feedback.BookFeedbacks
import com.example.bookcourt.models.metrics.DataClickMetric
import com.example.bookcourt.models.user.User
import com.example.bookcourt.utils.MetricType
import com.example.bookcourt.utils.MetricType.SKIP_BOOK
import com.example.bookcourt.utils.MetricType.DISLIKE_BOOK
import com.example.bookcourt.utils.ResultTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val metricRep: MetricsRepository,
    private val userRepositoryI: UserRepositoryI,
    private val basketRepository: BasketRepositoryI,
    private val bookApi: BooksApi,
) : ViewModel() {

    lateinit var user: User

    var bottomSheetState = mutableStateOf(false)
    val leaveFeedbackVisibility = mutableStateOf(true)
    val username = mutableStateOf("User name")
    val date = mutableStateOf("10 марта 2023")
    val title = mutableStateOf("")
    val rate = mutableStateOf(0)

    val isNeedToUpdateFeedback = mutableStateOf(false)

    val description = mutableStateOf("")

    var validBooks = mutableStateListOf<Book>()
    var dataIsReady by mutableStateOf(false)

    var isFirstDataLoading by mutableStateOf(true)
    var isNotificationDisplay = dataStoreRepository.getBoolPref(DataStoreRepository.isNotificationDisplay)
    var isAuthenticated = dataStoreRepository.getBoolPref(DataStoreRepository.isAuthenticated)
    var isFirstNotification = mutableStateOf(false)

    private var sessionTime = System.currentTimeMillis().toInt()
    private var fetchJob: Job? = null

    val limitSwipeValue = 3
    var counter by mutableStateOf(0)

    val json = Json { encodeDefaults = true }

    fun bookToString(book: Book):String{
        val item = book.isbn?.let {
            BookDto(
                id = it,
                data = BookData(
                    name=book.bookInfo.title,
                    author=book.bookInfo.author,
                    description=book.bookInfo.description,
                    createdAt="13213",
                    numberOfPage=book.bookInfo.numberOfPages,
                    rate=book.bookInfo.rate,
                    owner=book.shopOwner,
                    genre=book.bookInfo.genre,
                    image=book.bookInfo.image,
                    loadingAt="123",
                    price=book.bookInfo.price,
                    shop_owner=book.shopOwner,
                    buy_uri=book.buyUri,
                )
            )
        }
        return json.encodeToString(item)
    }


    var blurValueRecommendationScreen = Animatable(0f)
    var isBlur by mutableStateOf(false)

    fun displayNotificationMessage(){
        viewModelScope.launch {
            blurValueRecommendationScreen.snapTo(20f)
            isBlur = true
        }
    }

    fun closeNotificationMessage(){
        viewModelScope.launch {
            blurValueRecommendationScreen.snapTo(0f)
            isBlur = false
        }
    }

    fun getAllBooksRemote() {
        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val books = bookApi.getRecommendationBooks(10).execute().body()!!
//                bookApi.getRecommendationBooks(10).execute().code() == 200
//                val test = 5;
//            }catch(e:Exception){
//                // TODO("SET VISIBLE ERROR PAGE ")
//            }
        }

    }
    fun getAllBooks(context: Context) {
        getAllBooksRemote()
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

    private fun booksValidation(user: User, allBooks: List<Book>) {
        val readBooks = user.readBooksList.map { it.bookInfo }
        if (readBooks.isEmpty()) {
            validBooks.addAll(allBooks)
        } else {
            val items = allBooks.filter { book ->
                book.bookInfo !in readBooks
            }
            validBooks.addAll(items)
        }

    }

    private suspend fun convertBooksJsonToList(context: Context): List<Book> {
        val json = networkRepository.getAllBooks(context)!!
        val data = Json.decodeFromString<MutableList<BookDto>>(json)
        return data.map { it.toBook() }
    }

    private suspend fun getUser(): User {
        val userId = dataStoreRepository.getPref(uuid)
        user = userRepositoryI.loadData(userId.first())!!
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
            metricRep.appTime(sessionTime, MetricType.SCREEN_SESSION_TIME, "Recommendation")
            sessionTime = System.currentTimeMillis().toInt()
        }
    }

    fun countEqualToLimit(){
        viewModelScope.launch(Dispatchers.IO) {
            isFirstNotification.value = true
            dataStoreRepository.setPref(true,DataStoreRepository.isNotificationDisplay)
        }

    }

    fun updateUserStatistic(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepositoryI.updateData(user)
        }
    }

    fun addToBasket(item:BasketItem){
        viewModelScope.launch(Dispatchers.IO) {
            basketRepository.addData(item)
        }
    }

}