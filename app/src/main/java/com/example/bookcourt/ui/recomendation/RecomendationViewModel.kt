package com.example.bookcourt.ui.recomendation

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.isTutorChecked
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.models.Book
import com.example.bookcourt.models.BookRemote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class RecomendationViewModel @Inject constructor(
    val repository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var allBooks = mutableStateOf<List<Book>?>(null)
    val isEmpty = mutableStateOf(false)
    val tutorState = dataStoreRepository.getBoolState(isTutorChecked)

    fun getAllBooks() {
        val jobMain = viewModelScope.launch(Dispatchers.IO) {
            val job = async { repository.getAllBooks()!! }
            val json = job.await()
            val data = Json.decodeFromString<List<BookRemote>>("""$json""")
            allBooks.value = data.map {
                it.toBook()
            }
        }
    }

    private var tutorStateBool by mutableStateOf(false)

    fun editTutorState() {
        tutorStateBool = true
        viewModelScope.launch {
            dataStoreRepository.setPref(tutorStateBool, isTutorChecked)
        }
    }

}