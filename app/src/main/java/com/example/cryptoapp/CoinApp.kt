package com.example.cryptoapp

import android.app.Application
import com.example.cryptoapp.persistence.api.AppContainer
import com.example.cryptoapp.persistence.api.DefaultApp

class CoinApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultApp(this)
    }
}