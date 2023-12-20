package com.example.cryptoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cryptoapp.persistence.cache.CoinDao
import com.example.cryptoapp.persistence.cache.CoinDatabase
import com.example.cryptoapp.persistence.cache.CoinEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainCryptoViewModel(private val cryptoDao: CoinDao? = null) : ViewModel() {
    private val _cryptoList = MutableStateFlow<List<CoinEntity>>(emptyList())
    val cryptoList: MutableStateFlow<List<CoinEntity>> = _cryptoList

    init {
        loadCryptos()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CoinApp)
                MainCryptoViewModel(
                    cryptoDao = CoinDatabase.getDatabase(application).cryptoDao()
                )
            }
        }
    }

    private fun loadCryptos() {
        viewModelScope.launch {
            // Fetch the cached data from Room
            cryptoDao?.getAllCryptos()?.collect { cryptoEntities ->
                _cryptoList.value = cryptoEntities
            }
        }
    }

}






