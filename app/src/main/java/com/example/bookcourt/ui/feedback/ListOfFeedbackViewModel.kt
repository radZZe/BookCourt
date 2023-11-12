package com.example.bookcourt.ui.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookcourt.data.api.BooksApi
import com.example.bookcourt.models.feedback.ReviewRetrofit
import com.example.bookcourt.models.feedback.UserFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListOfFeedbackViewModel @Inject constructor(
    private val bookApi:BooksApi,
): ViewModel() {
    val listUserFeedbacks = listOf<UserFeedback>(
        UserFeedback(
            "user name",
            "Морские города имеют" +
                    " особое очарование. Родиться в одном из них – настоящая удача! И как же хочется, чтобы " +
                    "малыш скорее узнал о соленом ветре, высоких сопках и морских приключениях!",
            rate = 2,
            "15 сентября 2023"
        ),
        UserFeedback(
            "user name",
            "Морские города имеют" +
                    " особое очарование. Родиться в одном из них – настоящая удача! И как же хочется, чтобы " +
                    "малыш скорее узнал о соленом ветре, высоких сопках и морских приключениях!",
            rate = 3,
            "15 сентября 2023"
        ),
        UserFeedback(
            "user name",
            "Морские города имеют" +
                    " особое очарование. Родиться в одном из них – настоящая удача! И как же хочется, чтобы " +
                    "малыш скорее узнал о соленом ветре, высоких сопках и морских приключениях!",
            rate = 1,
            "15 сентября 2023"
        ),
        UserFeedback(
            "user name",
            "Морские города имеют" +
                    " особое очарование. Родиться в одном из них – настоящая удача! И как же хочется, чтобы " +
                    "малыш скорее узнал о соленом ветре, высоких сопках и морских приключениях!",
            rate = 4,
            "15 сентября 2023"
        ),
        UserFeedback(
            "user name",
            "Морские города имеют" +
                    " особое очарование. Родиться в одном из них – настоящая удача! И как же хочется, чтобы " +
                    "малыш скорее узнал о соленом ветре, высоких сопках и морских приключениях!",
            rate = 5,
            "15 сентября 2023"
        )
    )
    var listUserFeedbacksTest = listOf<ReviewRetrofit>()
    fun getFeedbacks(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            val response = bookApi.getReviews("92709a69-9f07-41a3-92ab-a78ee91bfe12").execute()
            if(response.isSuccessful){
                listUserFeedbacksTest = response.body()!!
                val test = 45
            }
        }

    }
}