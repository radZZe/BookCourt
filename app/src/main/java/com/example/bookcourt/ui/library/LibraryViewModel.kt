package com.example.bookcourt.ui.library

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.models.BookRemote
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.categorySelection.Category
import com.example.bookcourt.utils.Constants.genres
import com.example.bookcourt.utils.Partners
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    val networkRepository: NetworkRepository
):ViewModel() {
    val categories = genres.map { mutableStateOf(Category(it, mutableStateOf(false))) }
        .toMutableStateList()
    val partners = Partners.partners
    private val popularBooks = mutableStateListOf<Book?>()
    private val recommendationBooks = mutableStateListOf<Book?>()
    val popularBooksFiltered = mutableStateListOf<Book?>()
    val recommendationBooksFiltered = mutableStateListOf<Book?>()
    private val selectedCategory = mutableStateOf<Category?>(null)

    fun loadPopularBooks(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val json = networkRepository.getAllBooks(context)!!
            val data = Json.decodeFromString<MutableList<BookRemote>>(json)
            popularBooks.addAll(data.map { it.toBook() })
            popularBooksFiltered.addAll(data.map { it.toBook() })
        }
    }

    fun loadRecommendations(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val json = networkRepository.getAllBooks(context)!!
            val data = Json.decodeFromString<MutableList<BookRemote>>(json)
            recommendationBooks.addAll(data.map { it.toBook() })
            recommendationBooksFiltered.addAll(data.map { it.toBook() })
        }
    }

    fun filterCategories(filter:String){
        val category = categories.find { it.value.title==filter }!!
        if (category.value==selectedCategory.value){
            recommendationBooksFiltered.apply {
                clear()
                addAll(recommendationBooks)
            }
           popularBooksFiltered.apply {
                clear()
                addAll(popularBooks)
           }
            selectedCategory.value!!.state.value = false
            selectedCategory.value = null
        }
        else{
            selectedCategory.value?.state?.value = false
            recommendationBooksFiltered.apply {
                clear()
                addAll(recommendationBooks.filter {
                    it!!.bookInfo.genre==filter
                })
            }
            popularBooksFiltered.apply {
                clear()
                addAll(popularBooks.filter {
                    it!!.bookInfo.genre==filter
                })
            }
            selectedCategory.value = category.value
            category.value.state.value = !(category.value.state.value)
        }
    }
}