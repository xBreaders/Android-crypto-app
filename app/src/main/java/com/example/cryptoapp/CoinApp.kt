package com.example.cryptoapp

import android.app.Application
import com.example.cryptoapp.persistence.api.AppContainer
import com.example.cryptoapp.persistence.api.DefaultApp
import com.example.cryptoapp.persistence.api.SharedViewModel

class CoinApp : Application() {
    lateinit var container: AppContainer
    lateinit var sharedVM: SharedViewModel
    override fun onCreate() {
        super.onCreate()
        sharedVM = SharedViewModel()
        container = DefaultApp(this, sharedVM)

    }
}