package com.example.cryptoapp.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.persistence.api.DefaultCoinRepository
import com.example.cryptoapp.persistence.cache.CoinEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchCryptoViewModel(
    private val coinRepository: DefaultCoinRepository // Inject your CoinRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<CoinEntity>>(emptyList())
    val searchResults: StateFlow<List<CoinEntity>> = _searchResults.asStateFlow()

    fun searchCrypto(query: String) {
        viewModelScope.launch {
            // Assuming `coinRepository` has a method to fetch all cryptos
            val allCryptos = coinRepository.()
            _searchResults.value = allCryptos.filter {
                it.name.contains(query, ignoreCase = true) || it.symbol.contains(
                    query,
                    ignoreCase = true
                )
            }
        }
    }
}
