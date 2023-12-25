package com.example.cryptoapp

import com.example.cryptoapp.persistence.api.CoinData

sealed interface MainCryptoStatus {
    data class Error(val message: String) : MainCryptoStatus
    object Loading : MainCryptoStatus
    object Success : MainCryptoStatus
}

data class MainCryptoState(val coinData: List<CoinData> = listOf())
