package com.example.bookcourt.ui.search

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.data.room.searchRequest.SearchRequestRepository
import com.example.bookcourt.models.BookRemote
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.user.SearchRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRequestRepository: SearchRequestRepository,
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _recentRequests = mutableStateListOf<SearchRequest>()
    val recentRequests = MutableStateFlow(_recentRequests)

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private var _allBooks = mutableStateListOf<Book>()
    private val _books = MutableStateFlow(_allBooks)

    private val _isDisplayed = MutableStateFlow(false)
    val isDisplayed = _isDisplayed.asStateFlow()

    var recommendedBooks = listOf<Book>()

    @OptIn(FlowPreview::class)
    val books = searchText
        .debounce(timeoutMillis = 500L)
        .onEach { _isSearching.update { true } }
        .combine(_books) { text, books ->
            if (text.isBlank()) {
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
    }

    fun onClearSearchText() {
        _searchText.value = ""
    }

    fun getSearchRequests() {
        viewModelScope.launch(Dispatchers.IO) {
            _recentRequests.addAll(
                searchRequestRepository.getRequests()
                    .reversed()
                    .ifEmpty { listOf() }
            )
        }
    }

    fun addSearchRequest(currentRequest: String) {
        if (currentRequest.isNotBlank() &&
            _recentRequests.find { it.request == _searchText.value } == null
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                searchRequestRepository.addRequest(SearchRequest(request = currentRequest.trim()))
            }
        }
    }

    /* No API, just mockup */
    // TODO Перенести этот блок на новую реализацию networkRepository с Retrofit
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
        val data = Json.decodeFromString<MutableList<BookRemote>>(json)
        return data.map { it.toBook() }
    }
}