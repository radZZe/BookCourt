package com.example.bookcourt.ui.basket.basketScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.api.BooksApi
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.room.basket.BasketRepositoryI
import com.example.bookcourt.data.room.user.UserRepositoryI
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.basket.OwnerBasketItem
import com.example.bookcourt.models.book.Book
import com.example.bookcourt.models.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val repository: BasketRepositoryI,
    private val dataStoreRepository: DataStoreRepository,
    private val userRepositoryI: UserRepositoryI,
): ViewModel() {
    private val _flowBasketItems =  MutableStateFlow(emptyList<BasketItem>())
    val flowBaksetItems = _flowBasketItems.asStateFlow()
    var basketItems = mutableStateListOf<BasketItem>()
    val repositoryI = repository
    val owners = mutableStateListOf<OwnerBasketItem>()
    val stateSelectAll = mutableStateOf(false)


    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.getData().flowOn(Dispatchers.IO).collect { list ->
                list.forEach {
                    if (!itemInOwners(it.data.shopOwner)) {
                        owners.add(
                            OwnerBasketItem(
                                value = it.data.shopOwner,
                                isSelected = false,
                            )
                        )
                    }
                }
                _flowBasketItems.value = list
            }
        }
        isBasketItemsHasSelected()
    }




    fun increaseTheAmount(index:Int) {
        val item = _flowBasketItems.value[index].copy(amount =_flowBasketItems.value[index].amount + 1 )
        viewModelScope.launch(Dispatchers.IO) {
           repositoryI.updateData(item)
        }
    }



    fun reduceTheAmount(index: Int) {
        if (_flowBasketItems.value[index].amount > 1) {
            val item = _flowBasketItems.value[index].copy(amount = _flowBasketItems.value[index].amount-1)
            viewModelScope.launch(Dispatchers.IO) {
                repositoryI.updateData(item)
            }
        }

    }
    fun selectAll(){
        val list = mutableListOf<BasketItem>()
        for( i in 0..basketItems.size-1){
            basketItems[i] = basketItems[i].copy(isSelected = !stateSelectAll.value)
            list.add(basketItems[i])
        }
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.updateItems(list)
        }
        for (i in 0..owners.size - 1) {
            owners[i] = owners[i].copy(isSelected = stateSelectAll.value)
        }
    }

    fun changeItemSelectState(index: Int) {
        val item = _flowBasketItems.value[index].copy(isSelected = !_flowBasketItems.value[index].isSelected)
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.updateData(item)
        }
        var indexOwner = 0
        owners.forEachIndexed { indexItem,it ->
            it.value == _flowBasketItems.value[index].data.shopOwner
            indexOwner = indexItem
        }
        if(checkStateOwner(_flowBasketItems.value[index].data.shopOwner)){
            owners[indexOwner] = owners[indexOwner].copy(isSelected = true)
        }else{
            owners[indexOwner] = owners[indexOwner].copy(isSelected = false)
        }
        stateSelectAll.value = checkStateAll()
    }

    private fun checkStateOwner(owner: String): Boolean {
        _flowBasketItems.value.forEach {
            if (!it.isSelected && it.data.shopOwner == owner) {
                return false
            }
        }
        return true
    }

    private fun checkStateAll(): Boolean {
        _flowBasketItems.value.forEach {
            if (!it.isSelected) {
                return false
            }
        }
        return true
    }

    fun deleteBasketItem(item: BasketItem){
        basketItems.remove(item)
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.deleteData(item)
        }

    }

    fun deleteSelected(){
        val list = mutableListOf<BasketItem>()
        basketItems.removeIfCallback(condition = {
            if(it.isSelected){
                list.add(it)
                true
            }else{
                false
            }
        })
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.deleteItems(list)
        }

    }

    private fun checkOwnerSize(owner: String) {
        var count = 0
        _flowBasketItems.value.forEach {
            if(it.data.shopOwner == owner){
                count+=1;
            }
        }
        if(count <= 1){
            owners.removeIf {
                it.value == owner
            }
        }
    }

    fun changeItemSelectStateByOwner(owner: String, index: Int) {
        owners[index] = owners[index].copy(isSelected = !owners[index].isSelected)
        val list = mutableListOf<BasketItem>()
        for(i in 0 until basketItems.size){
            if(basketItems[i].data.shopOwner == owner){
                basketItems[i] = basketItems[i].copy(isSelected = owners[index].isSelected)
                list.add(basketItems[i])
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.updateItems(list)
        }
    }


    fun itemInOwners(item: String): Boolean {
        if (owners.size == 0) {
            return false
        } else {
            for (owner in owners) {
                if (owner.value == item) {
                    return true
                }
            }
            return false
        }
    }

    fun isBasketItemsHasSelected():Boolean {
        for (item in _flowBasketItems.value) {
            if (item.isSelected) {
                return  true
            }
        }
        return false
    }

    fun getPrice(): Int {
        var price = 0
        for (i in 0.. _flowBasketItems.value.size - 1) {
            if (_flowBasketItems.value[i].isSelected) {
                price += _flowBasketItems.value[i].data.bookInfo.price
            }
        }
        return price
    }

    fun getSelectedItems(): Int {
        var count = 0
        for (i in 0.. _flowBasketItems.value.size - 1) {
            if (_flowBasketItems.value[i].isSelected) {
                count += 1
            }
        }
        return count
    }

    private fun updateUserStatistic(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            userRepositoryI.updateData(user)
        }
    }
    private suspend fun getUser(): User {
        val userId = dataStoreRepository.getPref(DataStoreRepository.uuid)
        return userRepositoryI.loadData(userId.first())!!
    }

    fun addToFavorite(item: Book){
        viewModelScope.launch(Dispatchers.IO) {
            val user = getUser()
            if(item in user.wantToRead){
                user.wantToRead.remove(item)

            }else{
                user.wantToRead.add(item)
            }
            updateUserStatistic(user)
        }
    }


}