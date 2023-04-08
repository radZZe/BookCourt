package com.example.bookcourt.ui.recommendation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.user.UserRepositoryI
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardStackViewModel @Inject constructor(
    val dataStoreRepository: DataStoreRepository,
    private val userRepositoryI: UserRepositoryI
) : ViewModel() {

    var isEmpty by mutableStateOf(false)
    var allBooks:List<MutableState<Book>> = listOf()
    var i by mutableStateOf(allBooks.size - 1)
    var currentItem = if(allBooks.isNotEmpty()) allBooks[i] else null

    fun updateUserStatistic(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepositoryI.updateData(user)
        }
    }
}