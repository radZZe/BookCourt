package com.example.bookcourt.models.user

data class Notification(
    val id: String,  // TODO check compatibility with GUID
    val title: String,
    val text: String,
    var viewed: Boolean
)
