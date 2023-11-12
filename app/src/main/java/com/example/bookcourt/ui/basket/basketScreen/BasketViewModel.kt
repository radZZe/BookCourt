package com.example.bookcourt.ui.basket.basketScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.room.basket.BasketRepositoryI
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.basket.OwnerBasketItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    repository: BasketRepositoryI
) : ViewModel() {

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

    fun selectAll() {
        stateSelectAll.value = !stateSelectAll.value
        for (i in 0.._flowBasketItems.value.size - 1) {
            val item = _flowBasketItems.value[i].copy(isSelected = stateSelectAll.value)
            viewModelScope.launch(Dispatchers.IO) {
                repositoryI.updateData(item)
            }
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

    fun deleteBasketItem(item: BasketItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryI.deleteData(item)
        }

    }

    fun deleteSelected() {
        _flowBasketItems.value.forEach {
            if (it.isSelected) {
                checkOwnerSize(it.data.shopOwner)
                viewModelScope.launch(Dispatchers.IO) {
                    repositoryI.deleteData(it)
                }
            }
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
        for (i in 0 until _flowBasketItems.value.size) {
            if (_flowBasketItems.value[i].data.shopOwner == owner) {
                val item = _flowBasketItems.value[i].copy(isSelected = owners[index].isSelected )
                viewModelScope.launch(Dispatchers.IO) {
                    repositoryI.updateData(item)
                }
            }
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


}