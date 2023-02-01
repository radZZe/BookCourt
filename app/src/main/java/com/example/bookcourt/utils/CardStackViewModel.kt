package com.example.bookcourt.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.MetricsRepository
import com.example.bookcourt.models.Book
import com.example.bookcourt.models.UserAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject

@HiltViewModel
class CardStackViewModel @Inject constructor(
    val dataStoreRepository: DataStoreRepository,
    private val metricRep:MetricsRepository
) : ViewModel() {



    fun likeBook(genre:String){
        viewModelScope.launch(Dispatchers.IO) {
            val likedBooks =
                withContext(Dispatchers.Default) {
                    dataStoreRepository.getPrefInt(DataStoreRepository.savedLikedBooksCnt)
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

    private fun getTopGenres(){

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
            if (savedGenres.isNotBlank()){
                genres = toList(savedGenres)
            }
            genres.add(genre)
            dataStoreRepository.setPref(fromList(genres),DataStoreRepository.dislikedGenresList)
            Log.d("Danull","Disliked: $genres")
        }
    }

    fun wantToRead(bookTitle: String){
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
}