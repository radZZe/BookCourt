package com.example.bookcourt.ui.feedback

import androidx.lifecycle.ViewModel
import com.example.bookcourt.models.feedback.UserFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListOfFeedbackViewModel @Inject constructor(): ViewModel() {
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
    fun getFeedbacks(){
        //TODO
    }
}