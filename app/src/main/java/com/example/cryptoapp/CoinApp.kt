package com.example.cryptoapp

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.cryptoapp.persistence.API.AppContainer
import com.example.cryptoapp.persistence.API.DefaultApp
import com.example.cryptoapp.persistence.scheduler.CryptoWorker
import java.util.concurrent.TimeUnit

class CoinApp : Application() {
    private lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultApp()

        val constraints =
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val workRequest = PeriodicWorkRequestBuilder<CryptoWorker>(5, TimeUnit.MINUTES)
            .addTag("crypto-worker")
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}