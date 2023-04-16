package com.example.bookcourt.ui.search

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.models.BookDto
import com.example.bookcourt.models.book.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val networkRepository: NetworkRepository
) : ViewModel() {
    var recentRequests = mutableListOf<String>()

    private val _isDisplayed = MutableStateFlow(false)
    val isDisplayed = _isDisplayed.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private var _allBooks = mutableStateListOf<Book>()
    private val _books = MutableStateFlow(_allBooks)

    var recommendedBooks = listOf<Book>()
    val books = searchText
        .debounce(timeoutMillis = 1000L)
        .onEach { _isSearching.update { true } }
        .combine(_books) { text, books ->
            if(text.isBlank()) {
                books
            } else {
                books.filter { book ->
                    book.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach {
            _isSearching.update { false }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 1000L),
            _books.value
        )

    fun onSearchTextChange(text: String) {
        _isDisplayed.update { true }
        _searchText.value = text
        updateRecentRequests(text)
    }

    fun onClearSearchText() {
        _isDisplayed.update { false }
        _searchText.value = ""
    }

    fun getRecentRequests() {
        viewModelScope.launch(Dispatchers.IO) {
            val requests = dataStoreRepository.getPref(DataStoreRepository.recentRequestsList).first()
            recentRequests = requests.split(" ") as MutableList<String>
        }
    }

    fun updateRecentRequests(request: String) {
        viewModelScope.launch(Dispatchers.IO) {
            with(recentRequests) {
                reverse()
                add(request)
                if(size > 5) {
                    val deletableRange = size - 5
                    val newRequestsList = slice(deletableRange - 1 until size) as MutableList<String>
                    recentRequests = newRequestsList
                }
                reverse()
            }
            var requests = ""
            for (requestName in recentRequests) {
                requests += "$requestName "
            }
            dataStoreRepository.setPref(requests, DataStoreRepository.recentRequestsList)
        }
    }


    /* No API, just mockup */
    private var fetchJob: Job? = null
    fun getAllBooks(context: Context) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val allBooksItems = convertBooksJsonToList(context)
                recommendedBooks = allBooksItems
                _allBooks.addAll(allBooksItems)
            } catch (ioe: IOException) {
                Log.d("getAllBooks", "error cause ${ioe.cause}")
            }
        }
    }
    private suspend fun convertBooksJsonToList(context: Context): List<Book> {
        val json = networkRepository.getAllBooks(context)!!
        val data = Json.decodeFromString<MutableList<BookDto>>("""$json""")
        val allBooksItems = data.map { it.toBook() }
        return allBooksItems
    }
}