package com.example.bookcourt.models.ui

data class TutorialCard(
    val bookTitle: String = "Title",
    val bookAuthor: String = "Author",
//    val cover: String = "https://gl-img.rg.ru/resize800x533/uploads/photogallery/2022/07/07/oblozhka---god-literatury_a4c",
    val cover: Int = 1,
    val bottomText: String = "Выбирай движением",
    val bottomIcon: Int? = null,
    val swipe: String = "No"
)
