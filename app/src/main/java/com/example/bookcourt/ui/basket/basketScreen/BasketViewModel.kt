package com.example.bookcourt.ui.basket.basketScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.function.Predicate
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val repository: BasketRepositoryI,
    private val dataStoreRepository: DataStoreRepository,
    private val userRepositoryI: UserRepositoryI,
): ViewModel() {

    var basketItems = mutableStateListOf<BasketItem>()
    val _flowBasketItems = MutableStateFlow(emptyList<BasketItem>())
    val flowBasketItems = _flowBasketItems.asStateFlow()
    val repositoryI = repository
    val owners = mutableStateListOf<OwnerBasketItem>()
    val stateSelectAll = mutableStateOf(false)

//    init {
//        getItems()
//    }


    fun getItems(){
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.getData().flowOn(Dispatchers.IO).collect{ list ->

                if(list.isNotEmpty()){
                    //_flowBasketItems.update { list }
                    for(item in list){
                        if(!itemInOwners(item.data.shopOwner)){
                            owners.add(OwnerBasketItem(
                                value = item.data.shopOwner,
                                isSelected = false,
                            ))
                        }
                        if(!itemInBasketItems(item)){
                            basketItems.add(item)
                        }

                    }
                    owners.forEach {
                        if(checkStateOwner(it.value)){
                            var index = 0
                            owners.forEachIndexed { indexItem,item ->
                                if(it.value == owners[indexItem].value){
                                    index = indexItem
                                }

                            }
                            owners[index] = owners[index].copy(isSelected = true)
                        }
                    }
                    stateSelectAll.value = checkStateAll()
                }

            }



        }
    }
    fun increaseTheAmount(index: Int){
        basketItems[index] = basketItems[index].copy(amount = basketItems[index].amount+1)
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.updateData(basketItems[index])
        }

    }

    private fun itemInBasketItems(item:BasketItem):Boolean{
        basketItems.forEach {
            if(item.id == it.id){
                return true
            }
        }
        return false
    }

    fun reduceTheAmount(index: Int){
        if(basketItems[index].amount>1){
            basketItems[index] = basketItems[index].copy(amount = basketItems[index].amount-1)
            viewModelScope.launch(Dispatchers.IO) {
                repositoryI.updateData(basketItems[index])
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
        for(i in 0..owners.size-1){
            owners[i] = owners[i].copy(isSelected = !owners[i].isSelected)
        }
        stateSelectAll.value = !stateSelectAll.value
    }

    fun changeItemSelectState(index:Int){
        basketItems[index] = basketItems[index].copy(isSelected = !basketItems[index].isSelected)
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.updateData(basketItems[index])
        }
        var indexOwner = 0
        owners.forEachIndexed { indexItem,it ->
            it.value == basketItems[index].data.shopOwner
            indexOwner = indexItem
        }
        if(checkStateOwner(basketItems[index].data.shopOwner)){


            owners[indexOwner] = owners[indexOwner].copy(isSelected = true)
        }else{
            owners[indexOwner] = owners[indexOwner].copy(isSelected = false)
        }
        stateSelectAll.value = checkStateAll()
    }

    private fun checkStateOwner(owner:String):Boolean{
        basketItems.forEach {
            if(!it.isSelected && it.data.shopOwner == owner){
                return  false
            }
        }
        return true
    }
    private fun checkStateAll():Boolean{
        basketItems.forEach {
            if(!it.isSelected){
                return  false
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
    private fun SnapshotStateList<BasketItem>.removeIfCallback(condition:(it:BasketItem)->Boolean){
        this.removeIf {
            condition(it)
        }
    }

    fun changeItemSelectStateByOwner(owner:String,index:Int){
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


    fun itemInOwners(item:String):Boolean{
        if (owners.size ==0){
            return false
        }else{
            for(owner in owners){
                if(owner.value == item){
                    return true
                }
            }
            return false
        }
    }

    fun isBasketItemsHasSelected():Boolean{
        for(item in basketItems){
            if(item.isSelected){
                return true
            }
        }
        return false
    }

    fun getPrice():Int{
        var price = 0
        for(item in basketItems){
            if(item.isSelected){
                price += item.data.bookInfo.price
            }
        }
        return price
    }

    fun getSelectedItems():Int{
        var count = 0
        for(item in basketItems){
            if(item.isSelected){
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