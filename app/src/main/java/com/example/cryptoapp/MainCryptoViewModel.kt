package com.example.cryptoapp

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.cryptoapp.persistence.api.DefaultCoinRepository
import com.example.cryptoapp.persistence.scheduler.CryptoWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainCryptoViewModel(private val repository: DefaultCoinRepository, application: Application) :
    ViewModel() {
    private val _uiState = MutableStateFlow(MainCryptoState())
    var uiState: StateFlow<MainCryptoState> = _uiState
    private val workManager = WorkManager.getInstance(application)


    var apiState: MainCryptoStatus by mutableStateOf(MainCryptoStatus.Loading)
        private set

    init {
        loadCryptos()
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


        observeWorkStatus(workRequest.id)
    }

    private fun observeWorkStatus(workId: UUID) {
        workManager.getWorkInfoByIdLiveData(workId)
            .observeForever { workInfo ->
                if (workInfo.state.isFinished) {
                    loadCryptos()
                }
            }
    }


    private fun loadCryptos() {
        viewModelScope.launch {
            try {
                uiState = repository.getAllCoins().map { MainCryptoState(it) }.stateIn(
                    scope = viewModelScope,
                    started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000L),
                    initialValue = MainCryptoState()
                )
                apiState = MainCryptoStatus.Success
            } catch (e: Exception) {
                apiState = MainCryptoStatus.Error(e.message ?: "Unknown error")
            }
        }
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







