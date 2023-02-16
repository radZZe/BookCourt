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
    const val SWIPE_UP = "Swipe Up"
    const val SWIPE_DOWN = "Swipe Down"
    const val SWIPE_LEFT = "Swipe Left"
    const val SWIPE_RIGHT = "Swipe Right"
    const val APP_TIME = "App Time"
    const val SESSION_TIME = "Session Time"
    const val DEVICE_INFO = "Device Info"
    const val LOCATION = "Location"
    const val CLICK = "Click"
    const val USER_INFO = "User Info"
}

