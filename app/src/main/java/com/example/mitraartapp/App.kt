package com.example.mitraartapp

import android.app.Application

class App : Application() {
    val ecpService = ECPService()
    val lotService = LotService()
}