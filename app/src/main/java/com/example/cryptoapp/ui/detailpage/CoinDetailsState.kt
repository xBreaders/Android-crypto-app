package com.example.cryptoapp.ui.detailpage

import com.example.cryptoapp.persistence.api.CoinData
import com.example.cryptoapp.persistence.api.KLine

/**
 * Data class representing the state of a coin details within the application.
 *
 * @property isLoading Flags whether the coin data is currently loading.
 * @property coinDetails Holds the current coin data. It's nullable which may denote absence of data.
 * @property historicalData List of historical KLine data for the coin.
 * @property error Contains error information, if any occurred during the data processing.
 * @property selectedInterval Represents the selected timeframe for showing historical data ("1h" by default).
 */
data class CoinDetailsState(
    val isLoading: Boolean = false,
    val coinDetails: CoinData? = null,
    val historicalData: List<KLine> = emptyList(),
    val error: String? = null,
    val selectedInterval: String = "1h"
)

