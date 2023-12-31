package com.example.cryptoapp.ui.searchpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cryptoapp.CoinApp
import com.example.cryptoapp.persistence.api.CoinData
import com.example.cryptoapp.persistence.api.DefaultCoinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SearchCryptoViewModel(private val repository: DefaultCoinRepository) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<CoinData>>(emptyList())
    val searchResults = _searchResults
    val isLoading = MutableStateFlow(false)


    companion object {
        fun Factory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CoinApp
                SearchCryptoViewModel(
                    repository = application.container.repository,
                )
            }
        }
    }

    fun searchCrypto(query: String) {
        viewModelScope.launch {
            isLoading.value = true

            var searchQuery = repository.getCoinByQuery(query).firstOrNull()
            if (searchQuery!!.isNotEmpty()) {
                _searchResults.value = searchQuery
            } else {
                try {
                    searchQuery = listOf(repository.requestCoinBySymbol(query))
                    _searchResults.value = searchQuery
                } catch (e: Exception) {
                    searchQuery = emptyList()
                    _searchResults.value = searchQuery
                }
            }
            isLoading.value = false
        }
    }
}



