package com.example.cryptoapp.persistence.API

import com.example.cryptoapp.persistence.cache.CoinEntity

data class CryptoResponse(
    val data: List<CryptoData>,
    val status: Status
)

data class CryptoData(
    val id: Int,
    val name: String,
    val symbol: String,
    val rank: Int,
    val quote: Map<String, CryptoQuote>
) {
    fun toEntity(): CoinEntity {
        return CoinEntity(
            id = this.id,
            name = this.name,
            rank = this.rank,
            symbol = this.symbol,
            price = this.quote["USD"]?.price ?: 0.0,
            volume24h = this.quote["USD"]?.volume_24h ?: 0.0,
            percentChange24h = this.quote["USD"]?.percent_change_24h ?: 0.0,
            marketCap = this.quote["USD"]?.market_cap ?: 0.0
        )
    }
}

data class CryptoQuote(
    val price: Double,
    val volume_24h: Double,
    val percent_change_24h: Double,
    val market_cap: Double
)

data class Status(
    val timestamp: String,
    val error_code: Int,
    val error_message: String?,
    // ... other fields as per the API response
)

