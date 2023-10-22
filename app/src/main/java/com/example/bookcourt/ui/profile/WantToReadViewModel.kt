package com.example.bookcourt.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.room.basket.BasketRepositoryI
import com.example.bookcourt.data.room.user.UserRepositoryI
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WantToReadViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userRepositoryI: UserRepositoryI,
    private val repositoryI: BasketRepositoryI
) : ViewModel() {

    private var user: User? = null
    var wantToRead by mutableStateOf<MutableList<Book>?>(null)
    var basketItems = mutableStateListOf<BasketItem>()

    suspend fun getUser() {
        val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)
        user = userRepositoryI.loadData(userId.first())!!
        wantToRead = user?.wantToRead ?: mutableListOf<Book>()
        booksToBasketItems()
    }

    private fun bookToBasketItem(book: Book): BasketItem {
        return BasketItem(
            id = 123,
            data = book,
            amount = 0,
            isSelected = true
        )
    }

    private fun booksToBasketItems() {
        wantToRead?.map { bookToBasketItem(it) }?.let { basketItems.addAll(it) }
    }

    fun deleteBasketItem(item: BasketItem) {
        basketItems.remove(item)
        wantToRead?.remove(item.data)
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.deleteData(item)
            updateUserData()
        }
    }

    fun increaseTheAmount(index: Int) {
        basketItems[index] = basketItems[index].copy(amount = basketItems[index].amount + 1)
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.updateData(basketItems[index])
        }
        updateUserData()
    }

    fun reduceTheAmount(index: Int) {
        if (basketItems[index].amount > 0) {
            basketItems[index] = basketItems[index].copy(amount = basketItems[index].amount - 1)
            viewModelScope.launch(Dispatchers.IO) {
                repositoryI.updateData(basketItems[index])
            }
            updateUserData()
        }
    }

    fun changeItemSelectState(index:Int) {
        wantToRead?.let {
            if (basketItems[index].isSelected) {
                it.remove(basketItems[index].data)
            } else {
                it.add(basketItems[index].data)
            }
        }
        basketItems[index] = basketItems[index].copy(isSelected = !basketItems[index].isSelected)
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.updateData(basketItems[index])
        }
        updateUserData()
    }

    private fun updateUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            user?.let {
                it.wantToRead = wantToRead!!
                userRepositoryI.updateData(it)
            }
        }
    }

}