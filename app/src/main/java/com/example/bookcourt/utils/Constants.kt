package com.example.bookcourt.utils

import com.example.bookcourt.R
import com.example.bookcourt.models.ui.TutorialCard

object Constants {
    const val OTHER_CITY = "Другой"
    const val DATE_TIME_METRIC_FORMAT = "dd-MM-yyyy HH:mm:ss"
    const val LIMIT_WINDOW_HEIGHT = 1920

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

    val genres = listOf(
        "Детективы",
        "Комедия",
        "Трагедия",
        "Драма",
        "Детская литература",
        "Рассказы",
        "Фантастика",
        "Экономика",
        "Научная фантастика",
        "Фэнтези",
        "Зарубежная литература"
    )

    val tutorialCards = listOf(
        TutorialCard(
            bookTitle = "Это Владивосток, детка",
            bookAuthor = "Диана Лютер, Детская литература",
            cover = R.drawable.cover_this_is_vdk,
            bottomText = "Выбирай движением",
            bottomIcon = null,
            swipe = "No"
        ),
        TutorialCard(
            bookTitle = "Лис и Ча",
            bookAuthor = "Диана Лютер, Детская литература",
            cover = R.drawable.cover_fox_and_cha,
            bottomText = "Нравится",
            bottomIcon = R.drawable.heart_ic,
            swipe = "Right"
        ),
        TutorialCard(
            bookTitle = "Остров Владивосток",
            bookAuthor = "Диана Лютер, живопись и графика",
            cover = R.drawable.cover_island_vdk,
            bottomText = "Не нравится",
            bottomIcon = R.drawable.dislike_heart_ic,
            swipe = "Left"
        ),
        TutorialCard(
            bookTitle = "Эрмитаж",
            bookAuthor = "Диана Лютер, цветы и птицы",
            cover = R.drawable.cover_flowers_and_birds,
            bottomText = "Хочу прочитать",
            bottomIcon = R.drawable.favorite_ic,
            swipe = "Up"
        ),
        TutorialCard(
            bookTitle = "Лис и Ча",
            bookAuthor = "Диана Лютер, Детская литература",
            cover = R.drawable.cover_fox_and_cha,
            bottomText = "Нравится",
            bottomIcon = R.drawable.skip_ic,
            swipe = "Down"
        ),
    )

    val statisticScreensList = listOf("ReadBooks", "Lyuteratura", "FavoriteAuthors", "IgraSlov", "Zarya", "FavoriteGenres")

    val navMenuScreensList = listOf(BottomNavMenu.Profile, BottomNavMenu.Library, BottomNavMenu.Recommendations, BottomNavMenu.Basket)
}

object MetricType {
    const val SWIPE = "swipe"
    const val WANT_TO_READ_BOOK = "Want_to_read_book"
    const val SKIP_BOOK = "skip_book"
    const val DISLIKE_BOOK = "dislike_book"
    const val LIKE_BOOK = "like_book"
    const val APP_SESSION_TIME = "App Session Time"
    const val SCREEN_SESSION_TIME = "Session"
    const val DEVICE_INFO = "Device_Info"
    const val LOCATION = "Location"
    const val CLICK = "Click"
    const val USER_INFO = "User_Info"
}

object Buttons {
    const val BUY_BOOK = "Buy Book"
    const val BOOK_CARD = "Book Card"
    const val OPEN_STATS = "Open Stats"
    const val SWAP_STAT = "Swap stat"
    const val CLOSE = "Close"
    const val STATS_NOTIFICATION = "Check Stats"
    const val SIGN_IN = "Sign In"
    const val CHECK_TUTOR = "Check Tutor"
    const val VERIFY_CODE = "Verify Code"
    const val CATEGORIES_SELECTION = "Pick Categories"
}

object AppVersion {
    const val appVersion = "0.9.5"
}

object Partners{
    val partners = listOf(
        Sponsors.Lyuteratura,
        Sponsors.Zarya,
        Sponsors.IgraSlov
    )
}

object ApiUrl{
    const val METRICS_URL = "http://2f65-77-34-189-143.jp.ngrok.io/api/"
    const val BOOKS_URL = "https://bookcourttest-ee89c-default-rtdb.asia-southeast1.firebasedatabase.app/"//"https://www.jsonkeeper.com/b/"
}

object MapApi{
    const val API_KEY = "69136701-b14f-40d4-9225-6d8b7242c623"
}


