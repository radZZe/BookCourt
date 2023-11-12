package com.example.bookcourt.ui.search

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.api.SearchApi

import com.example.bookcourt.data.room.searchRequest.SearchRequestRepository
import com.example.bookcourt.models.search.SearchBook
import com.example.bookcourt.models.user.SearchRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRequestRepository: SearchRequestRepository,
    private val searchApi: SearchApi
) : ViewModel() {

    private val _recentRequests = mutableStateListOf<SearchRequest>()
    val recentRequests = MutableStateFlow(_recentRequests)

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private var _allBooks = mutableStateListOf<SearchBook>()
    private val _books = MutableStateFlow(_allBooks)

    private val _isDisplayed = MutableStateFlow(false)
    val isDisplayed = _isDisplayed.asStateFlow()

    var recommendedBooks = listOf<SearchBook>()

    @OptIn(FlowPreview::class)
    val books = _books.asStateFlow()
        .debounce(timeoutMillis = 500L)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 1000L),
            _books.value
        )

    fun onSearchTextChange(text: String) {
        _isDisplayed.update { text != "" }
        _searchText.value = text
        getBooks(text)
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

    private var currentJob:Job?=null

    private fun getBooks(query:String){
        currentJob?.cancel()
        currentJob = viewModelScope.launch(Dispatchers.IO) {
            _isSearching.update { true }
            try {
                val searchResponse = searchApi.searchBooks(query)
                _allBooks.clear()
                _allBooks.addAll(searchResponse.books)
                recommendedBooks = searchResponse.otherBooks
            } catch (e:retrofit2.HttpException){
                _allBooks.clear()
                recommendedBooks = emptyList()
            } finally {
                _isSearching.update { false }
            }

        }
    }
}