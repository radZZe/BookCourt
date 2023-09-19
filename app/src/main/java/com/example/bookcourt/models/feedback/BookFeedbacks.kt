package com.example.bookcourt.models.feedback

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable

@Serializable
data class BookFeedbacks(
    var isUserLeaveFeedback:Boolean = false,
    var countOfFeedbacks:Int,
    var userFeedback: UserFeedback? = null,
){
    fun leaveAFeedback(userFeedback: UserFeedback){
        this.userFeedback = userFeedback
        this.countOfFeedbacks = this.countOfFeedbacks + 1
        this.isUserLeaveFeedback = true
    }
}
