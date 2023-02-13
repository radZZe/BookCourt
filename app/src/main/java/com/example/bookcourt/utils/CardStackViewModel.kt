package com.example.bookcourt.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.data.room.UserRepository
import com.example.bookcourt.models.Book
import com.example.bookcourt.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardStackViewModel @Inject constructor(
    val dataStoreRepository: DataStoreRepository,
    private val metricRep: MetricsRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)

    var direction = mutableStateOf<String?>(null)
    var currentItem: MutableState<Book?> = mutableStateOf<Book?>(null)
//    lateinit var currentUser: User

    var readBooks = mutableListOf<Book>()
    var wantToRead = mutableListOf<Book>()

    fun getReadBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            val job = async { userRepository.getUserById(userId.first()) }
//            currentUser = user
            var user = job.await()
            readBooks = user.readBooksList as MutableList<Book>
            wantToRead = user.wantToRead as MutableList<Book>
        }
    }

    fun updateUserStatistic() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = userRepository.getUserById(userId.first())
            val updatedUser = updateUserLists(currentUser, readBooks, wantToRead)
            userRepository.updateUser(updatedUser)
        }
    }

    private suspend fun updateUserLists(
        user: User,
        readBooksList: List<Book>,
        wantToReadList: List<Book>
    ): User {
        user.readBooksList = readBooksList
        user.wantToRead = wantToReadList
        return user
    }

    fun changeCurrentItem(item: Book) {
        currentItem.value = item
    }

//    fun likeBook(genre:String){
//        viewModelScope.launch(Dispatchers.IO) {
//            val likedBooks =
//                withContext(Dispatchers.Default) {
//                    dataStoreRepository.getIntPref(DataStoreRepository.savedLikedBooksCnt)
//                }.first()+1
//            val favoriteGenres =
//                withContext(Dispatchers.Default) {
//                    dataStoreRepository.getPref(DataStoreRepository.savedFavoriteGenres)
//                }.first()
//            val genres = generateFavoriteGenres(favoriteGenres,genre)
//            dataStoreRepository.setPref(fromMap(genres),DataStoreRepository.savedFavoriteGenres)
//            Log.d("Danull","Liked books cnt: $likedBooks")
//            dataStoreRepository.setPref(likedBooks,DataStoreRepository.savedLikedBooksCnt)
//            Log.d("Danull","favorite genres: ${genres.entries}")
//        }
//    }

//    private fun generateFavoriteGenres(favoriteGenres:String,genre: String):Map<String,Int>{
//        if (favoriteGenres.isNotBlank()){
//            var genres = toMap(favoriteGenres)
//            val cnt = genres.getOrElse(genre){
//                genres[genre] = 0
//               0
//            }
//            genres[genre] = cnt+1
//            genres = genres.toList().sortedBy {
//                it.second
//            }.asReversed().toMap().toMutableMap()
//            return genres
//        }
//        else{
//          return mapOf(genre to 1)
//        }
//
//    }

//    fun dislikeBook(genre: String){
//        viewModelScope.launch(Dispatchers.IO) {
//            var genres = arrayListOf<String>()
//            val savedGenres =
//                withContext(Dispatchers.Default) {
//                    dataStoreRepository.getPref(DataStoreRepository.dislikedGenresList)
//                }.first()
//            val dislikedBooksCnt =  withContext(Dispatchers.Default) {
//                dataStoreRepository.getIntPref(DataStoreRepository.savedDislikedBooksCnt)
//            }.first() + 1
//            if (savedGenres.isNotBlank()){
//                genres = toList(savedGenres)
//            }
//            genres.add(genre)
//            dataStoreRepository.setPref(fromList(genres),DataStoreRepository.dislikedGenresList)
//            dataStoreRepository.setPref(dislikedBooksCnt,DataStoreRepository.savedDislikedBooksCnt)
//            Log.d("Danull","Disliked: $genres")
//            Log.d("Danull","Disliked cnt : $dislikedBooksCnt")
//        }
//    }
//
//    fun wantToRead(bookTitle: String){  //REFACTOR
//        viewModelScope.launch(Dispatchers.IO) {
//            var genres = arrayListOf<String>()
//            val savedGenres =
//                withContext(Dispatchers.Default) {
//                    dataStoreRepository.getPref(DataStoreRepository.savedWantToReadList)
//                }.first()
//            if (savedGenres.isNotBlank()){
//                genres = toList(savedGenres)
//            }
//            genres.add(bookTitle)
//            dataStoreRepository.setPref(fromList(genres),DataStoreRepository.savedWantToReadList)
//            Log.d("Danull","Want to read: $genres")
//        }
//    }

//    fun readBooks(bookTitle: String){
//        viewModelScope.launch(Dispatchers.IO) {
//            var books = arrayListOf<String>()
//            val readBooks =
//                withContext(Dispatchers.Default) {
//                    dataStoreRepository.getPref(readBooksList)
//                }.first()
//            if (readBooks.isNotBlank()){
//                books = toList(readBooks)
//            }
//            books.add(bookTitle)
//            dataStoreRepository.setPref(fromList(books), readBooksList)
//            Log.d("Danull","Read: $books")
//        }
//    }

    fun getColorSwipe(direction: String?): Brush? {
        if (direction == DIRECTION_BOTTOM) {
            val colorStopsBottom = arrayOf(
                0.0f to Color(0.3f, 0f, 0.41f, 0.75f),
                0.8f to Color.Transparent
            )

            var brush =
                Brush.verticalGradient(colorStops = colorStopsBottom, startY = 0f, endY = 1f)
            return brush
        } else if (direction == DIRECTION_RIGHT) {
            val colorStopsRight = arrayOf(
                0.0f to Color(0.3f, 0.55f, 0.21f, 0.75f),
                0.8f to Color.Transparent
            )
            var brush =
                Brush.horizontalGradient(colorStops = colorStopsRight, startX = 0f, endX = 1f)
            return brush
        } else if (direction == DIRECTION_TOP) {
            val colorStopsTop = arrayOf(
                0.0f to Color(1f, 0.6f, 0f, 0.75f),
                0.8f to Color.Transparent
            )
            var brush = Brush.verticalGradient(colorStops = colorStopsTop, startY = 1f, endY = 0f)
            return brush
        } else if (direction == DIRECTION_LEFT) {
            val colorStopsLeft = arrayOf(
                0.0f to Color(1f, 0.31f, 0.31f, 0.75f),
                0.8f to Color.Transparent
            )
            var brush =
                Brush.horizontalGradient(colorStops = colorStopsLeft, startX = 0f, endX = 1f)
            return brush
        } else {
            return null
        }
    }

    fun changeDirection(newDirection: String?, item: Book?) {
        if (item != null) {
            item.onSwipeDirection = newDirection
        }
    }
}