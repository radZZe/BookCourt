package com.example.bookcourt.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.bookcourt.data.repositories.DataStoreRepository
import com.example.bookcourt.data.room.user.UserRepositoryI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userRepositoryI: UserRepositoryI
) : ViewModel() {
    var option by mutableStateOf<OrderOptions?>(null)

    fun onOptionChanged(newText: OrderOptions) {
        option = newText
    }
}

enum class OrderOptions {
    ACTIVE, BOUGHT
}