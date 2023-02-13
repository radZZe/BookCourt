package com.example.bookcourt.ui.statistics

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.data.room.UserRepository
import com.example.bookcourt.models.Book
import com.example.bookcourt.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.stream.Collectors.toList
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val repository: NetworkRepository,
    private val userRepository: UserRepository,
    val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)

    var user = mutableStateOf<User?>(null)

    val readBooks = mutableStateOf<MutableList<Book>?>(null)
    val wantToRead = mutableStateOf<MutableList<Book>?>(null)
    lateinit var favGenresList: List<String>

    fun getUserStats() {
        val job = viewModelScope.launch(Dispatchers.IO) {
                user = mutableStateOf(userRepository.getUserById(userId.first()))
                user.value?.let { getReadBooksList(it) }
                user.value?.let { getWantedBooksList(it) }
        }
        runBlocking {
            job.join()
            user.value?.let { getTopGenres(it) }
        }

    }

    suspend fun getReadBooksList(user: User) {
        readBooks.value =  user.readBooksList  as MutableList<Book>?
    }

    suspend fun getWantedBooksList(user: User) {
        wantToRead.value = user.wantToRead as MutableList<Book>?
    }

    fun getTopGenres(user: User): List<String> {
        var topGenreMap = mutableMapOf<String, Int>()
        for (book in user.readBooksList) {
            if (topGenreMap.containsKey(book.bookInfo.genre)) {
                var count = topGenreMap[book.bookInfo.genre]!!
                topGenreMap[book.bookInfo.genre] = (count + 1)
            } else {
                topGenreMap[book.bookInfo.genre] = 1
            }
        }
        topGenreMap.toList().sortedByDescending { (_, value) -> value }.toMap()
        return topGenreMap.keys.toList()
    }

}

//    suspend fun getTopGenres(): List<String> {
//        val genresList = mutableListOf("Отсутствует", "Отсутствует", "Отсутствует")
//        val genres = dataStoreRepository.getPref(DataStoreRepository.savedFavoriteGenres).first()
//        var genresMap = emptyMap<String, Int>()
//        if (genres.isNotBlank()) {
//            genresMap = Json.decodeFromString(genres)
//        }
//        var cnt = 1
//        for (element in genresMap) {
//            if (cnt == 1) {
//                genresList[0] = element.key
//            } else if (cnt == 2) {
//                genresList[1] = element.key
//            } else if (cnt == 3) {
//                genresList[2] = element.key
//            } else {
//                break
//            }
//            cnt++
//        }
//        topGenres = genresList
//        return genresList
//    }

//    private suspend fun generateStatistics(): UserStatistics {
//        val likedBooks =
//            dataStoreRepository.getIntPref(DataStoreRepository.savedLikedBooksCnt).first()
//        val wantToRead = dataStoreRepository.getPref(DataStoreRepository.savedWantToReadList).first()
//        var wantToReadGenres = emptyList<String>()
//        if (wantToRead.isNotBlank()) {
//            wantToReadGenres = Json.decodeFromString<List<String>>(wantToRead)
//        }
//        val disliked = dataStoreRepository.getPref(DataStoreRepository.dislikedGenresList).first()
//        val dislikedBooksCnt =
//            dataStoreRepository.getIntPref(DataStoreRepository.savedDislikedBooksCnt).first()
//        var dislikedGenres = emptyList<String>()
//        if (disliked.isNotBlank()){
//            dislikedGenres =Json.decodeFromString<List<String>>(disliked)
//        }
//        val favoriteGenres =  getTopGenres()
//        return UserStatistics(
//            likedBooks+dislikedBooksCnt,
//            likedBooks,
//            dislikedBooksCnt,
//            favoriteGenres,
//            wantToReadGenres,
//            "Unknown"
//        )
//    }


//    fun getUserData(context: Context) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val job = async { repository.getUserData(context) }
//            val json = job.await()
//            val data = Json.decodeFromString<UserRemote>("""$json""")
//            val statistics = async { generateStatistics() }.await()
//            user.value = data.toUser().apply {
//                this.statistics = statistics
//            }
//        }
//    }

//    fun shareStats(context: Context,) {
//        val sendIntent: Intent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(
//                Intent.EXTRA_TEXT,
//                context.getString(R.string.user_statistics_in_the_app) + "\n"
//
//                        + context.getString(R.string.number_of_books_read_by_the_user) + "${user.value?.statistics?.numberOfReadBooks}"
//                        + "\n"
//                        + context.getString(R.string.books_user_liked) + "${user.value?.statistics?.numberOfLikedBooks}"
//                        + "\n" + context.getString(R.string.users_favorite_genres) + " ${user.value?.statistics?.favoriteGenreList?.get(0)} , " +
//                        "${user.value?.statistics?.favoriteGenreList?.get(1)} , " +
//                        "${user.value?.statistics?.favoriteGenreList?.get(2)} " +
//                        "\n${getWantedBooks(user.value?.statistics?.wantToRead ?: listOf(""))}"
//            )
//            type = "text/plain"
//        }
//        val shareIntent = Intent.createChooser(sendIntent, null)
//        context.startActivity(shareIntent)
//    }

//}