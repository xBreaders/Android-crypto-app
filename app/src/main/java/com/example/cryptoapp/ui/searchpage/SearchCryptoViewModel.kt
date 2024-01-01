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


/**
 * ViewModel for managing and storing UI-related data in the lifecycle of the Search Crypto screen.
 *
 * This ViewModel is used to perform search operation over cryptocurrency data and handle the search results.
 *
 * @property repository The repository which provides coin data.
 * @property _searchResults The private mutable state flow for search results, used internally for updating the search results in the ViewModel.
 * @property searchResults The public state flow representing the search results from the latest search query.
 * @property isLoading The state flow representing the current loading status of searching for cryptocurrencies.
 */
class SearchCryptoViewModel(private val repository: DefaultCoinRepository) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<CoinData>>(emptyList())
    val searchResults = _searchResults
    val isLoading = MutableStateFlow(false)

    /**
     * Companion object used to instantiate the ViewModel with specific parameters using a factory method.
     */
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

    /**
     * Performs a search operation over the cryptocurrency data.
     * Updates the `searchResults` state flow with the search results
     * and `isLoading` to indicate the end of the search operation.
     *
     * @param query Search query
     */
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



