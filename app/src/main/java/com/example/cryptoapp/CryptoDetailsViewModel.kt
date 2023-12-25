package com.example.cryptoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cryptoapp.persistence.api.DefaultCoinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CryptoDetailsViewModel(private val repository: DefaultCoinRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CoinDetailsState())
    var uiState: StateFlow<CoinDetailsState> = _uiState


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CoinApp
                CryptoDetailsViewModel(
                    repository = application.container.repository
                )
            }
        }
    }

    fun fetchCoinDetails(coinId: Int) {
        viewModelScope.launch {
            uiState = repository.getCoinById(coinId).map { CoinDetailsState(it) }.stateIn(
                scope = viewModelScope,
                started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000L),
                initialValue = CoinDetailsState()
            )
        }
    }
}
