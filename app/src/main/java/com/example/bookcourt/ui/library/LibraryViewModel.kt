package com.example.bookcourt.ui.library

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.api.LibraryApi
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.models.BookDto
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.book.BookRetrofit
import com.example.bookcourt.models.categorySelection.Category
import com.example.bookcourt.models.library.BookBlock
import com.example.bookcourt.models.library.InfoBlock
import com.example.bookcourt.utils.Constants.genres
import com.example.bookcourt.utils.Partners
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val libraryApi: LibraryApi
):ViewModel() {
    val categories = genres.map { mutableStateOf(Category(it, mutableStateOf(false))) }
        .toMutableStateList()
    val infoBLocks = mutableStateListOf<InfoBlock>()
    val bookBlocks = mutableStateListOf<BookBlock>()
    val bookBlocksFiltered = mutableListOf<MutableState<List<BookRetrofit>>>()
    private val selectedCategory = mutableStateOf<Category?>(null)
    fun loadCatalog() {
        viewModelScope.launch(Dispatchers.IO) {
            val catalogResponse = libraryApi.fetchCatalog()
            infoBLocks.addAll(catalogResponse.infoBlocks)
            bookBlocks.addAll(catalogResponse.bookBlocks)
            for(book in bookBlocks){
                bookBlocksFiltered.add(mutableStateOf(book.blockItems))
            }
        }
    }

    fun filterCategories(filter:String){
        val category = categories.find { it.value.title==filter }!!
        if (category.value==selectedCategory.value){
            for (i in bookBlocks.indices){
                bookBlocksFiltered[i].value = bookBlocks[i].blockItems
            }
            selectedCategory.value!!.isSelected.value = false
            selectedCategory.value = null
        }
        else{
            selectedCategory.value?.isSelected?.value = false
            for (i in bookBlocks.indices){
                bookBlocksFiltered[i].value =bookBlocks[i].blockItems.filter {
                    it.genre==filter
                }
            }
            selectedCategory.value = category.value
            category.value.isSelected.value = !(category.value.isSelected.value)
        }
    }
}