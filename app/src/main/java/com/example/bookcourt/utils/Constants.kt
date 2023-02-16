package com.example.bookcourt.utils

object Constants {
    const val OTHER_CITY = "Другой"
    val cities = listOf(
        "Хабаровск",
        "Владивосток",
        "Биробиджан",
        "Москва",
        "Санкт-Петербург",
        "Тюмень",
        "Казань",
        "Уссурийск",
        "Краснодар",
        OTHER_CITY
    )
}

object MetricType {
    const val WANT_TO_READ_BOOK = "Want_to_read_book"
    const val SKIP_BOOK = "skip_book"
    const val DISLIKE_BOOK = "dislike_book"
    const val LIKE_BOOK = "like_book"
    const val APP_TIME = "App_Time"
    const val SESSION_TIME = "Session_Time"
    const val DEVICE_INFO = "Device_Info"
    const val LOCATION = "Location"
    const val CLICK = "Click"
    const val USER_INFO = "User_Info"
}

