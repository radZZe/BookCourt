package com.example.bookcourt.ui.profile

import androidx.lifecycle.ViewModel
import com.example.bookcourt.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class OrdersNotificationViewModel @Inject constructor(

) : ViewModel() {
    val notifications =
        listOf<OrderNotification>(OrderNotification(), OrderNotification(), OrderNotification())
}

data class OrderNotification(
    val icon: Int = R.drawable.ic_notification_fill,
    val header: String = "Заголовок уведомления",
    val text: String = "Текст уведомления"
)

