package com.example.bookcourt.ui.recomendation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val repository: NetworkRepository
):ViewModel() {
    var allBooks = mutableStateOf<List<Book>?>(null)
     fun getAllBooks(){
        val jobMain = viewModelScope.launch(Dispatchers.IO) {
            val job = async{repository.getAllBooks()!!}
            val json = job.await()
            val data = Json.decodeFromString<List<BookRemote>>("""$json""")
            allBooks.value = data.map {
                it.toBook()
            }
        }
    }
}