package com.example.bookcourt.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.room.user.UserRepositoryI
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class WantToReadViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userRepositoryI: UserRepositoryI
) : ViewModel() {

    private var user: User? = null
    var wantToRead by mutableStateOf<MutableList<Book>?>(null)

    suspend fun getUser() {
        val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)
        user = userRepositoryI.loadData(userId.first())!!
        wantToRead = user?.wantToRead ?: mutableListOf<Book>()
    }

}