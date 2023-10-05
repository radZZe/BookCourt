package com.example.bookcourt.models.basket

data class OwnerBasketItem(
    val value:String,
    val isSelected:Boolean,
){
    fun inOwners(owner:String):Boolean{
        return this.value == owner
    }
}
