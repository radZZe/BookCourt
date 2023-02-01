package com.example.bookcourt.ui.profile

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedName
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedPhoneNumber
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.savedSurname
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.models.Statistics
import com.example.bookcourt.models.User
import com.example.bookcourt.models.UserRemote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repository: NetworkRepository,
    val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    val user = mutableStateOf<User?>(null)
    val alertDialogState = mutableStateOf(false)
    val feedbackState = mutableStateOf(false)
    val statisticsState = mutableStateOf(false)
    val feedbackData = mutableStateOf("")
    val feedbackMessage = mutableStateOf("")

    val userName = dataStoreRepository.getPref(savedName)
    val userSurname = dataStoreRepository.getPref(savedSurname)
    val userPhone = dataStoreRepository.getPref(savedPhoneNumber)

    fun dismiss() {
        if (feedbackState.value) {
            feedbackStateChanged()
        } else {
            statisticsStateChanged()
        }
    }

    fun statisticsStateChanged() {
        statisticsState.value = !statisticsState.value
        alertDialogState.value = !alertDialogState.value
    }

    fun feedbackStateChanged() {
        feedbackState.value = !feedbackState.value
        alertDialogState.value = !alertDialogState.value
    }

    fun feedbackDataChanged(str: String) {
        feedbackData.value = str
    }

    fun feedbackMessageChanged(str: String) {
        feedbackMessage.value = str
    }

    private suspend fun getTopGenres(): List<String> {
        val genresList = mutableListOf("Отсутствует","Отсутствует","Отсутствует")
            val genres = dataStoreRepository.getPref(DataStoreRepository.savedFavoriteGenres).first()
            var genresMap = emptyMap<String,Int>()
            if (genres.isNotBlank()){
                genresMap = Json.decodeFromString(genres)
            }
            var cnt = 1
           for (element in genresMap){
               if (cnt==1){
                   genresList[0] = element.key
               }
               else if (cnt==2){
                   genresList[1] = element.key
               }
               else if (cnt==3){
                   genresList[2] = element.key
               }
               else{
                   break
               }
               cnt++
           }
        return genresList
    }

    private suspend fun generateStatistics():Statistics{
            val likedBooks =
                dataStoreRepository.getPrefInt(DataStoreRepository.savedLikedBooksCnt).first()
            val wantToRead = dataStoreRepository.getPref(DataStoreRepository.savedWantToReadList).first()
            var wantToReadGenres = emptyList<String>()
            if (wantToRead.isNotBlank()) {
                wantToReadGenres = Json.decodeFromString<List<String>>(wantToRead)
            }
            val disliked = dataStoreRepository.getPref(DataStoreRepository.dislikedGenresList).first()
            var dislikedGenres = emptyList<String>()
            if (disliked.isNotBlank()){
                dislikedGenres =Json.decodeFromString<List<String>>(disliked)
            }
            val favoriteGenres =  getTopGenres()
            return Statistics(
                0,
                likedBooks,
                favoriteGenres,
                wantToReadGenres,
                dislikedGenres,
                "Unknown"
            )
    }

    fun getUserData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val job = async { repository.getUserData(userId) }
            val json = job.await()
            val data = Json.decodeFromString<UserRemote>("""$json""")
            val statistics = async { generateStatistics() }.await()
            user.value = data.toUser().apply {
                this.statistics = statistics
            }
        }
    }
}