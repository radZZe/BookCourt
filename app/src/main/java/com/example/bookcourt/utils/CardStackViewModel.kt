package com.example.bookcourt.utils

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.DataStoreRepository.PreferenceKeys.readBooksList
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.models.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import javax.inject.Inject

@HiltViewModel
class CardStackViewModel @Inject constructor(
    val dataStoreRepository: DataStoreRepository,
    private val metricRep:MetricsRepository
) : ViewModel() {

    //state flags
    var isEmpty by mutableStateOf(false)
    var isNotificationDisplay = mutableStateOf(false)
    var isBookInfoDisplay = mutableStateOf(false)

    var allBooks:List<MutableState<Book>> = listOf()
    var i by mutableStateOf(allBooks.size - 1)
    var direction = mutableStateOf<String?>(null)
    var counter by mutableStateOf(0)

    var currentItem = if(allBooks.isNotEmpty()) allBooks[i] else null //КОСТЫЛЬ


    fun changeCurrentItem(){
        if(i!=-1){
            currentItem = allBooks[i]
        }else{
            isEmpty = true
        }

    }

    fun countEqualToLimit(){
        isNotificationDisplay.value = !isNotificationDisplay.value
    }


    fun likeBook(genre:String){
        viewModelScope.launch(Dispatchers.IO) {
            val likedBooks =
                withContext(Dispatchers.Default) {
                    dataStoreRepository.getIntPref(DataStoreRepository.savedLikedBooksCnt)
                }.first()+1
            val favoriteGenres =
                withContext(Dispatchers.Default) {
                    dataStoreRepository.getPref(DataStoreRepository.savedFavoriteGenres)
                }.first()
            val genres = generateFavoriteGenres(favoriteGenres,genre)
            dataStoreRepository.setPref(fromMap(genres),DataStoreRepository.savedFavoriteGenres)
            Log.d("Danull","Liked books cnt: $likedBooks")
            dataStoreRepository.setPref(likedBooks,DataStoreRepository.savedLikedBooksCnt)
            Log.d("Danull","favorite genres: ${genres.entries}")
        }
    }

    private fun generateFavoriteGenres(favoriteGenres:String,genre: String):Map<String,Int>{
        if (favoriteGenres.isNotBlank()){
            var genres = toMap(favoriteGenres)
            val cnt = genres.getOrElse(genre){
                genres[genre] = 0
               0
            }
            genres[genre] = cnt+1
            genres = genres.toList().sortedBy {
                it.second
            }.asReversed().toMap().toMutableMap()
            return genres
        }
        else{
          return mapOf(genre to 1)
        }

    }

    private fun toMap(value:String):MutableMap<String,Int>{
        return Json.decodeFromString(value)
    }

    private fun fromMap(map:Map<String,Int>):String{
        return   Json.encodeToJsonElement(map).toString()
    }

    private fun toList(value:String):ArrayList<String>{
        return Json.decodeFromString(value)
    }

    private fun fromList(list: List<String>):String{
        return   Json.encodeToJsonElement(list).toString()
    }

    fun dislikeBook(genre: String){
        viewModelScope.launch(Dispatchers.IO) {
            var genres = arrayListOf<String>()
            val savedGenres =
                withContext(Dispatchers.Default) {
                    dataStoreRepository.getPref(DataStoreRepository.dislikedGenresList)
                }.first()
            val dislikedBooksCnt =  withContext(Dispatchers.Default) {
                dataStoreRepository.getIntPref(DataStoreRepository.savedDislikedBooksCnt)
            }.first() + 1
            if (savedGenres.isNotBlank()){
                genres = toList(savedGenres)
            }
            genres.add(genre)
            dataStoreRepository.setPref(fromList(genres),DataStoreRepository.dislikedGenresList)
            dataStoreRepository.setPref(dislikedBooksCnt,DataStoreRepository.savedDislikedBooksCnt)
            Log.d("Danull","Disliked: $genres")
            Log.d("Danull","Disliked cnt : $dislikedBooksCnt")
        }
    }

    fun wantToRead(bookTitle: String){  //REFACTOR
        viewModelScope.launch(Dispatchers.IO) {
            var genres = arrayListOf<String>()
            val savedGenres =
                withContext(Dispatchers.Default) {
                    dataStoreRepository.getPref(DataStoreRepository.savedWantToReadList)
                }.first()
            if (savedGenres.isNotBlank()){
                genres = toList(savedGenres)
            }
            genres.add(bookTitle)
            dataStoreRepository.setPref(fromList(genres),DataStoreRepository.savedWantToReadList)
            Log.d("Danull","Want to read: $genres")
        }
    }

    fun readBooks(bookTitle: String){
        viewModelScope.launch(Dispatchers.IO) {
            var books = arrayListOf<String>()
            val readBooks =
                withContext(Dispatchers.Default) {
                    dataStoreRepository.getPref(readBooksList)
                }.first()
            if (readBooks.isNotBlank()){
                books = toList(readBooks)
            }
            books.add(bookTitle)
            dataStoreRepository.setPref(fromList(books), readBooksList)
            Log.d("Danull","Read: $books")
        }
    }

    fun changeDirection(newDirection:String?,item: MutableState<Book>?){
//        allBooks.last().value.onSwipeDirection.value = newDirection
        if(item != null){
            item.value.onSwipeDirection = newDirection
        }

    }
}