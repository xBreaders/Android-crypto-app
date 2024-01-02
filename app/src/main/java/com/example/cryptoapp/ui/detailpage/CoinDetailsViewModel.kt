package com.example.cryptoapp.ui.detailpage

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cryptoapp.CoinApp
import com.example.cryptoapp.persistence.api.DefaultCoinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/**
 * ViewModel for managing and storing UI-related data in the lifecycle of the coin details.
 *
 * @property repository The repository from which to retrieve coin data.
 * @property coinId The ID of the coin to fetch and display.
 *
 * @property _uiState Private mutable state flow representing UI state. Holds CoinDetailsState objects.
 * @property uiState Read-only state flow returned to external observers.
 */
class CoinDetailsViewModel(
    private val repository: DefaultCoinRepository,
    private val coinId: Int,
    private val ConnectionContext: Context
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(CoinDetailsState())
    var uiState: StateFlow<CoinDetailsState> = _uiState

    /**
     * Companion object used to instantiate the ViewModel with specific parameters using a factory method.
     * Provides coinId to the ViewModel.
     */
    companion object {
        fun Factory(coinId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CoinApp
                CoinDetailsViewModel(
                    repository = application.container.repository,
                    coinId = coinId,
                    ConnectionContext = application.applicationContext
                )
            }
        }
    }

    /**
     * Method to fetch coin details from the repository and update the UI state accordingly.
     * @param coinId Unique ID of the coin.
     */
    init {
        fetchCoinDetails(coinId)
    }

    private fun isNetworkAvailable(context: Context) =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            } ?: false
        }

    /**
     * Method to fetch coin details from the repository and update the UI state accordingly.
     * @param coinId Unique ID of the coin.
     */
    private fun fetchCoinDetails(coinId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val coinDetailsResponse = repository.getCoinById(coinId).firstOrNull()
            if (coinDetailsResponse == null) {
                _uiState.update {
                    it.copy(
                        error = "Failed to fetch coin details",
                        isLoading = false
                    )
                }
                return@launch
            }

            _uiState.update { it.copy(coinDetails = coinDetailsResponse) }

            if (coinDetailsResponse.symbol.equals("usdt", ignoreCase = true)) {
                _uiState.update {
                    it.copy(
                        error = "No charting available for USDT",
                        isLoading = false
                    )
                }
                return@launch
            }

            if (!isNetworkAvailable(ConnectionContext)) {
                _uiState.update {
                    it.copy(
                        error = "Only prices and market info is available when offline",
                        isLoading = false
                    )
                }
                return@launch
            }

            try {
                val historicalResponse = repository.getKLinesBySymbol(
                    coinDetailsResponse.symbol + "USDT",
                    _uiState.value.selectedInterval
                )
                _uiState.update { it.copy(historicalData = historicalResponse, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.localizedMessage ?: "Network error occurred",
                        isLoading = false
                    )
                }
            }
        }
    }
}



