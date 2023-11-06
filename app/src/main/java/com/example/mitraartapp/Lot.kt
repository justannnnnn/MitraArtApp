package com.example.mitraartapp

import android.graphics.drawable.Drawable

data class Lot(
    val id: Long, // Уникальный номер пользователя
    val name: String, // Имя произведения
    val author: String, // Автор
    val price: Double, // Цена
    val image: String // картинка
)
