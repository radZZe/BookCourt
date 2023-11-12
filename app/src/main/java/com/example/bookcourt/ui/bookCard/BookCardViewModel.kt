package com.example.bookcourt.ui.bookCard

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.api.BooksApi
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.repositories.NetworkRepository
import com.example.bookcourt.data.room.basket.BasketRepositoryI
import com.example.bookcourt.data.room.user.UserRepositoryI
import com.example.bookcourt.models.BookDto
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.book.BookRetrofit
import com.example.bookcourt.models.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BookCardViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val basketRepository: BasketRepositoryI,
    private val dataStoreRepository: DataStoreRepository,
    private val bookApi:BooksApi,
    private val userRepositoryI: UserRepositoryI,
):ViewModel() {
    val isAuthenticated = dataStoreRepository.getBoolPref(DataStoreRepository.isAuthenticated)
    var book = mutableStateOf<Book?>(null)
    private var fetchJob: Job? = null
    val rate = mutableStateOf(0)
    val description: MutableState<String?> = mutableStateOf(null)
    val isActiveBasket = mutableStateOf(true)
    val isFavorite = mutableStateOf(false)
    val errorPage = mutableStateOf(false)


    private suspend fun convertBooksJsonToList(context: Context): List<Book> {
        val json = networkRepository.getAllBooks(context)!!
        val data = Json.decodeFromString<MutableList<BookDto>>(json)
        return data.map { it.toBook() }
    }

    fun getBookByID(context: Context,id:String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val allBooksItems = convertBooksJsonToList(context)
                allBooksItems.filter {
                    id == it.isbn
                }
                book.value = allBooksItems.firstNotNullOf { item -> item.takeIf { it.isbn == id } }
                isInBasket(book.value!!)
                isBookFavorite(book.value!!)
            } catch (ioe: IOException) {
                Log.d("getAllBooks", "error cause ${ioe.cause}")
            }
        }
    }

    fun testGetBookById(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val book: BookRetrofit = bookApi.getBookById(id).execute().body()!!
            }catch (exc:Exception){
                Log.d("CODE","!=200")
                errorPage.value = true
            }
        }
    }

    fun addBasketItem(item:BasketItem){
        viewModelScope.launch(Dispatchers.IO) {
            isActiveBasket.value = false
            basketRepository.addData(item)
        }
    }

    private fun isInBasket(item:Book){
        viewModelScope.launch(Dispatchers.IO) {
            val list = basketRepository.findData(item);
            isActiveBasket.value = list.isEmpty()
        }
    }

    fun addToFavorite(item:Book){
        viewModelScope.launch(Dispatchers.IO) {
            val user = getUser()
            if(isFavorite.value){
                user.wantToRead.remove(item)

            }else{
                user.wantToRead.add(item)
            }
            updateUserStatistic(user)
            isFavorite.value = !isFavorite.value

        }
        val test =5
    }
    private fun updateUserStatistic(user:User){
        viewModelScope.launch(Dispatchers.IO) {
            userRepositoryI.updateData(user)
        }
    }
    private suspend fun getUser(): User {
        val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)
        return userRepositoryI.loadData(userId.first())!!
    }

    fun isBookFavorite(item:Book){
        viewModelScope.launch(Dispatchers.IO) {
            val user = getUser()
            isFavorite.value = item in user.wantToRead
        }

    }

}