package com.example.cryptoapp.persistence.api

import com.example.cryptoapp.persistence.cache.CoinDetailsEntity
import com.example.cryptoapp.persistence.cache.CoinEntity
import com.google.gson.annotations.SerializedName

data class CryptoResponse(
    @SerializedName("data")
    val data: List<CoinData> = listOf(),
)

data class CoinData(
    val id: Int = 0,
    val name: String = "",
    val symbol: String = "",
    val slug: String = "",
    val cmc_rank: Int = 0,
    val num_market_pairs: Int = 0,
    val circulating_supply: Double = 0.0,
    val total_supply: Double = 0.0,
    val max_supply: Double? = null,
    val percent_change_24h: Double = 0.0,
    val infinite_supply: Boolean = false,
    val last_updated: String = "",
    val date_added: String = "",
    val tags: List<String> = listOf(),
    @SerializedName("quote")
    val quote: Map<String, CryptoQuote> = mapOf()
)

data class CryptoQuote(
    val price: Double = 0.0,
    val volume_24h: Double = 0.0,
    val volume_change_24h: Double = 0.0,
    val percent_change_24h: Double = 0.0,
    val market_cap: Double = 0.0,
    val fully_diluted_market_cap: Double = 0.0,
    val market_cap_dominance: Double = 0.0,
    val last_updated: String = ""
)

fun CoinData.asDatabaseEntity(): CoinEntity {
    return CoinEntity(
        id = id,
        name = name,
        symbol = symbol,
        rank = cmc_rank,
        price = quote["USD"]?.price ?: 0.0,
        percentChange24h = quote["USD"]?.percent_change_24h ?: 0.0,
        marketCap = quote["USD"]?.market_cap ?: 0.0,
        coinDetails = CoinDetailsEntity(
            coinId = id,
            circulatingSupply = circulating_supply,
            totalSupply = total_supply,
            maxSupply = max_supply,
            numMarketPairs = num_market_pairs,
            lastUpdated = last_updated,
            dateAdded = date_added,
            tags = tags.joinToString(","),
            slug = slug,
            infiniteSupply = infinite_supply,
            volume = quote["USD"]?.volume_24h ?: 0.0,
            fullyDilutedMarketCap = quote["USD"]?.fully_diluted_market_cap ?: 0.0,
            marketCapDominance = quote["USD"]?.market_cap_dominance ?: 0.0,
            quoteLastUpdated = quote["USD"]?.last_updated ?: ""
        )
    )
}


