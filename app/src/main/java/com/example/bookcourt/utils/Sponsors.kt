package com.example.bookcourt.utils

import com.example.bookcourt.R

sealed class Sponsors(
    val iconId:Int,
    val imageId:Int,
    val title:String,
    val mainText:String,
    val url:String
){
    object Lyuteratura:Sponsors(
        iconId = R.drawable.partner_lyuteratura_logo,
        imageId = R.drawable.partner_lyuteratura_content,
        title = "Любите детей, книги и творчество?",
        mainText = "Тогда не проходите мимо и загляните в детский книжный магазин “Лютература”.",
        url = "https://lyuteratura.ru/"
    )
    object Zarya:Sponsors(
        iconId = R.drawable.csi_zarya_logo,
        imageId = R.drawable.zarya_screen_cover,
        title = "Книги про искусство",
        mainText = "В ЦСИ \"Заря\" вы всегда найдете познавателььную и обучающую литературу, связанную с искусством.",
        url = "http://zaryavladivostok.ru/ru/shop/books"
    )
    object IgraSlov:Sponsors(
        iconId = R.drawable.igra_slov_logo_unfilled,
        imageId = R.drawable.igra_slov_screen_cover,
        title = "Вы точно найдете, что почитать",
        mainText = "Независимый книжный магазин \"Игра слов\", место, где редкие книги и кофе.",
        url = "https://igraslov.store/"
    )
}
