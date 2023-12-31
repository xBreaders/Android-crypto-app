package com.example.cryptoapp.ui.mainpage

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.cachedIn
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.cryptoapp.CoinApp
import com.example.cryptoapp.persistence.api.DefaultCoinRepository
import com.example.cryptoapp.persistence.scheduler.CryptoWorker
import java.util.concurrent.TimeUnit


class MainCryptoViewModel(private val repository: DefaultCoinRepository, application: Application) :
    ViewModel() {
    val pagedCoins = repository.getPagedCoins().cachedIn(viewModelScope)
    private val workManager = WorkManager.getInstance(application)


    init {
        scheduleAndObserveWorker()
    }

    private fun scheduleAndObserveWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest =
            PeriodicWorkRequestBuilder<CryptoWorker>(15, TimeUnit.MINUTES) // Minimum frequency
                .setConstraints(constraints)
                .build()

        workManager.enqueueUniquePeriodicWork(
            "crypto-sync",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CoinApp
                MainCryptoViewModel(
                    repository = application.container.repository,
                    application = application
                )
            }
        }
    }
}







