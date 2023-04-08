package com.example.bookcourt.ui.categorySelection

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.bookcourt.models.categorySelection.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategorySelectionViewModel @Inject constructor(

): ViewModel() {
    val selectedCategories = mutableStateListOf<MutableState<Category>>()
    val categories = mutableStateListOf<MutableState<Category>>(
        mutableStateOf(
            Category("Детективы", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Детская литература", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Рассказы", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Фантастика", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Экономика", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Научная фантастика", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Фэнтези", mutableStateOf(false))
        ),
        mutableStateOf(
            Category("Зарубежная литература", mutableStateOf(false))
        )
    )

    fun changeStateCategory(index: Int) {
        if(categories[index].value.state.value){
            categories[index].value.state.value = !categories[index].value.state.value
            selectedCategories.remove(categories[index])
        }else{
            categories[index].value.state.value = !categories[index].value.state.value
            selectedCategories.add(categories[index])
        }

    }



}