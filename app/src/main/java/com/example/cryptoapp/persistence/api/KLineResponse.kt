package com.example.cryptoapp.persistence.api

data class KLine(
    val openTime: Long, // Kline open time
    val open: Double, // Kline open price
    val high: Double,   // Kline high price
    val low: Double,    // Kline low price
    val close: Double,  // Kline close price
    val volume: Double, // Kline base asset volume
    val closeTime: Long,    // Kline close time
    val quoteAssetVolume: Double, // Kline quote asset volume
    val numberOfTrades: Int, // Number of trades
    val takerBuyBaseAssetVolume: Double, // Taker buy base asset volume
    val takerBuyQuoteAssetVolume: Double, // Taker buy quote asset volume
)


