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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    repository: BasketRepositoryI
): ViewModel() {

    var basketItems = mutableStateListOf<BasketItem>()
    val repositoryI = repository
    val owners = mutableStateListOf<OwnerBasketItem>()
    val stateSelectAll = mutableStateOf(false)


    fun getItems(){
        viewModelScope.launch(Dispatchers.IO) {
            var list = repositoryI.getData()
            for(item in list){
                if(!itemInOwners(item.data.shopOwner)){
                    owners.add(OwnerBasketItem(
                        value = item.data.shopOwner,
                        isSelected = false,
                    ))
                }

                basketItems.add(item)
            }
        }
    }
    fun increaseTheAmount(index: Int){
        basketItems[index] = basketItems[index].copy(amount = basketItems[index].amount+1)
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.updateData(basketItems[index])
        }

    }

    fun reduceTheAmount(index: Int){
        basketItems[index] = basketItems[index].copy(amount = basketItems[index].amount-1)
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.updateData(basketItems[index])
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