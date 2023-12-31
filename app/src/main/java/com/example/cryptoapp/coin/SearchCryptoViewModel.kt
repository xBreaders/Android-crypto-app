package com.example.cryptoapp.coin

import androidx.lifecycle.ViewModel
import com.example.cryptoapp.persistence.cache.CoinEntity
import kotlinx.coroutines.flow.MutableStateFlow

class SearchCryptoViewModel : ViewModel() {

    private val _searchResults = MutableStateFlow<List<CoinEntity>>(emptyList())

    /* fun searchCrypto(query: String) {
         viewModelScope.launch {
             // Assuming `coinRepository` has a method to fetch all cryptos
             val allCryptos = coinRepository.get
             _searchResults.value = allCryptos.filter {
                 it.name.contains(query, ignoreCase = true) || it.symbol.contains(
                     query,
                     ignoreCase = true
                 )
             }
         }*/
}

