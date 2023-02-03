package com.example.bookcourt.ui.statistics

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.R
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.models.User
import com.example.bookcourt.models.UserRemote
import com.example.bookcourt.ui.getWantedBooks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject


@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val repository: NetworkRepository,
    val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val user = mutableStateOf<User?>(null)

    fun getUserData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val job = async { repository.getUserData(context) }
            val json = job.await()
            val data = Json.decodeFromString<UserRemote>("""$json""")
            user.value = data.toUser()
        }
    }

    fun shareStats(context: Context, user: User) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.user_statistics_in_the_app) + "\n"

//                        + context.getString(R.string.number_of_books_read_by_the_user) + "${user.statistics.numberOfReadBooks}"
//                        + "\n"
//                        + context.getString(R.string.books_user_liked) + "${user.statistics.numberOfLikedBooks}"
//                        + "\n" + context.getString(R.string.users_favorite_genres) + " ${user.statistics.favoriteGenreList[0]} , " +
//                        "${user.statistics.favoriteGenreList[1]} , " +
//                        "${user.statistics.favoriteGenreList[2]} " +
//                        "\n${getWantedBooks(user.statistics.wantToRead)}"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }
}