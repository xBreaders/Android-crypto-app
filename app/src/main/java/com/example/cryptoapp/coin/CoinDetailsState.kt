package com.example.cryptoapp.coin

import com.example.cryptoapp.persistence.api.CoinData
import com.example.cryptoapp.persistence.api.KLine

data class CoinDetailsState(
    val isLoading: Boolean = false,
    val coinDetails: CoinData? = null,
    val historicalData: List<KLine> = emptyList(),
    val error: String? = null,
    val selectedInterval: String = "1h"
)

