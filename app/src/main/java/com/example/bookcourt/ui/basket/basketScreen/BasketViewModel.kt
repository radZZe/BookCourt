package com.example.bookcourt.ui.basket.basketScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.room.basket.BasketRepositoryI
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.basket.OwnerBasketItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    repository: BasketRepositoryI
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

        for( i in 0..basketItems.size-1){
            basketItems[i] = basketItems[i].copy(isSelected = !stateSelectAll.value)
            viewModelScope.launch(Dispatchers.IO) {
                repositoryI.updateData(basketItems[i])
            }
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
    fun updateBasketData(){

    }

    fun deleteBasketItem(item: BasketItem){
        basketItems.remove(item)
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.deleteData(item)
        }

    }

    fun deleteSelected(){
        basketItems.removeIf {
            it.isSelected
        }
    }

    fun changeItemSelectStateByOwner(owner:String,index:Int){
        owners[index] = owners[index].copy(isSelected = !owners[index].isSelected)
        for(i in 0 until basketItems.size){
            if(basketItems[i].data.shopOwner == owner){
                basketItems[i] = basketItems[i].copy(isSelected = owners[index].isSelected)
                viewModelScope.launch(Dispatchers.IO) {
                    repositoryI.updateData(basketItems[index])
                }
            }
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



}