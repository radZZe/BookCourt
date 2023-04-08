package com.example.bookcourt.models.categorySelection

import androidx.compose.runtime.MutableState

data class Category(
    var title:String,
    var state: MutableState<Boolean>
)
