package com.example.bookcourt.ui.basket.orderingScreen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.api.DeliveryApi
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.room.basket.BasketRepositoryI
import com.example.bookcourt.models.basket.BasketItem
import com.example.bookcourt.models.delivery.DeliveryBook
import com.example.bookcourt.models.delivery.DeliveryInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class OrderingScreenViewModel @Inject constructor(
    repositoryI: BasketRepositoryI,
    private val deliveryApi: DeliveryApi,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {


    val regionName = mutableStateOf("")
    val cityName = mutableStateOf("")
    val postOfficeIndex = mutableStateOf("")
    val address = mutableStateOf("")
    val isDialogShown = mutableStateOf(false)

    val basketItems = mutableStateListOf<BasketItem>()
    val repository = repositoryI
    val statePayment = mutableStateOf(true) // true - Юмонеу false - СБП
    fun getItems() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getData().flowOn(Dispatchers.IO).collect{list->
                list.forEach {
                    if (it.isSelected) {
                        basketItems.add(it)
                    }
                }
            }
        }
    }

    fun getAddress(){
        viewModelScope.launch (Dispatchers.IO){
            address.value = dataStoreRepository.getPref(DataStoreRepository.address).first()
            postOfficeIndex.value = dataStoreRepository.getPref(DataStoreRepository.postOfficeIndex).first()
            regionName.value = dataStoreRepository.getPref(DataStoreRepository.regionName).first()
            cityName.value = dataStoreRepository.getPref(DataStoreRepository.cityName).first()
        }
    }

    fun getPrice():Int{
        var price = 0
        for(item in basketItems){
            price += item.data.bookInfo.price
        }
        return price
    }

    fun changeStatePayment(state:Boolean){
        statePayment.value = state
    }

    fun getDeliveryInfo(){
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val books = mutableListOf<DeliveryBook>()
//                for(basketItem in basketItems){
//                    books.add(
//                        DeliveryBook(
//                            bookId = basketItem.data.isbn!!,
//                            count = basketItem.amount
//                        )
//                    )
//                }
                books.add(
                    DeliveryBook(
                    bookId = "92709a69-9f07-41a3-92ab-a78ee91bfe12",
                    count = 1
                ))//mock
                val deliveryInfo = DeliveryInfo(
                    regionName = regionName.value,
                    cityName = cityName.value,
                    address = address.value,
                    index =  postOfficeIndex.value,
                    books = books
                )
                val deliveryResponse = deliveryApi.getDeliveryPrice(deliveryInfo)
                Log.d("test",deliveryResponse.deliveryPrice.toString())
                dataStoreRepository.setPref(address.value,DataStoreRepository.address)
                dataStoreRepository.setPref(cityName.value,DataStoreRepository.cityName)
                dataStoreRepository.setPref(postOfficeIndex.value,DataStoreRepository.postOfficeIndex)
                dataStoreRepository.setPref(regionName.value,DataStoreRepository.regionName)

            }catch (e:HttpException){
                //todo
                Log.d("test",e.message.toString())
            }
        }
    }


}