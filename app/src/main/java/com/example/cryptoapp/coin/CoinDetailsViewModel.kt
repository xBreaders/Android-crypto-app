package com.example.cryptoapp.coin

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
import kotlinx.coroutines.launch

class CoinDetailsViewModel(private val repository: DefaultCoinRepository, private val coinId: Int) :
    ViewModel() {
    private val _uiState = MutableStateFlow(CoinDetailsState())
    var uiState: StateFlow<CoinDetailsState> = _uiState


    companion object {
        fun Factory(coinId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CoinApp
                CoinDetailsViewModel(
                    repository = application.container.repository,
                    coinId = coinId
                )
            }
        }
    }

    init {
        fetchCoinDetails(coinId)
    }

    private fun fetchCoinDetails(coinId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val coinDetailsResponse = repository.getCoinById(coinId).firstOrNull()
            if (coinDetailsResponse != null) {
                _uiState.value = _uiState.value.copy(coinDetails = coinDetailsResponse)

                try {
                    val historicalResponse = repository.getKLinesBySymbol(
                        coinDetailsResponse.symbol + "USDT",
                        _uiState.value.selectedInterval
                    )
                    if (historicalResponse.isSuccessful) {
                        val historicalData =
                            historicalResponse.body()?.map { parseKLine(it) } ?: emptyList()
                        _uiState.value = _uiState.value.copy(historicalData = historicalData)
                    } else {
                        _uiState.value =
                            _uiState.value.copy(error = "Failed to fetch historical data")
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(error = "Network error occurred")
                    //print out the error
                    e.printStackTrace()
                }
            } else {
                _uiState.value = _uiState.value.copy(error = "Failed to fetch coin details")
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    /*private fun parseKLine(kLineData: List<Any>): KLine {
        // Ensure this parsing logic matches the structure of your JSON response
        // Example:
        return KLine(
            openTime = kLineData[0].toString().toDouble().toLong(),
            open = kLineData[1].toString().toDouble(),
            high = kLineData[2].toString().toDouble(),
            low = kLineData[3].toString().toDouble(),
            close = kLineData[4].toString().toDouble(),
            volume = kLineData[5].toString().toDouble(),
            closeTime = kLineData[6].toString().toDouble().toLong(),
            quoteAssetVolume = kLineData[7].toString().toDouble(),
            numberOfTrades = kLineData[8] as Double,
            takerBuyBaseAssetVolume = kLineData[9].toString().toDouble(),
            takerBuyQuoteAssetVolume = kLineData[10].toString().toDouble()
        )
    }*/

}



