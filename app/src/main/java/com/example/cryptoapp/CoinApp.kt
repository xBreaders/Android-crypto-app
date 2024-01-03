package com.example.cryptoapp

import android.app.Application
import com.example.cryptoapp.persistence.api.AppContainer
import com.example.cryptoapp.persistence.api.DefaultApp
import com.example.cryptoapp.persistence.api.SharedViewModel
/**
 * Custom Application class for the App.
 *
 * This custom implementation allows central place for initializing shared resources upon application startup.
 *
 * @property container The AppContainer instance representing the application-level dependencies including the repository and retrofit-service.
 * @property sharedVM The SharedViewModel instance which holds shared data across multiple composables related to API response handling.
 */
class CoinApp : Application() {
    lateinit var container: AppContainer
    lateinit var sharedVM: SharedViewModel
    override fun onCreate() {
        super.onCreate()
        sharedVM = SharedViewModel()
        container = DefaultApp(this, sharedVM)

    }
}