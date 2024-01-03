package com.example.cryptoapp.persistence.api.response

/**
 * Data class representing a Kline or candlestick object for a specific timeframe.
 * Candlestick charts are often used in trading as they not only contain the same information as a bar chart,
 * but also visually represent the price development with a candle.
 *
 * @property openTime The time when this candle has been opened. Represented in milliseconds since the epoch.
 * @property open The price at which this candle has been opened.
 * @property high The highest price at which this candle traded.
 * @property low The lowest price at which this candle traded.
 * @property close The price at which this candle closed.
 * @property volume The volume traded during the lifetime of this candle.
 * @property closeTime The time when this candle closed.
 * @property quoteAssetVolume The volume of the quote asset traded.
 * @property numberOfTrades The number of trades that were made for this candle.
 * @property takerBuyBaseAssetVolume The volume of the base asset that has been bought by takers.
 * @property takerBuyQuoteAssetVolume The volume of the quote asset that has been bought by takers.
 */
data class KLine(
    val openTime: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double,
    val closeTime: Long,
    val quoteAssetVolume: Double,
    val numberOfTrades: Int,
    val takerBuyBaseAssetVolume: Double,
    val takerBuyQuoteAssetVolume: Double,
)