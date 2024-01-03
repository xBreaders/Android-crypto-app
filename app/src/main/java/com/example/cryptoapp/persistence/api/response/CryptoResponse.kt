package com.example.cryptoapp.persistence.api.response

import com.example.cryptoapp.persistence.cache.CoinDetailsEntity
import com.example.cryptoapp.persistence.cache.CoinEntity
import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat


/**
 * Data class representing the response from the CoinMarketCap API service.
 *
 * @property data A list of [CoinData] objects fetched from the CoinMarketCap API.
 */
data class CryptoResponse(
    @SerializedName("data")
    val data: List<CoinData> = listOf(),
)

/**
 * Data class representing a coin's data fetched from the CoinMarketCap API.
 *
 * This data class has various parameters that provide comprehensive information about a specific coin.
 *
 * @property id Unique id of the coin.
 * @property name Name of the coin.
 * @property symbol Symbol of the coin.
 * @property slug A slug is a user and SEO friendly short label containing only letters, numbers, hyphens or underscores.
 * @property cmc_rank Rank of the coin on CoinMarketCap.
 * @property num_market_pairs Number of market pairs for the coin.
 * @property circulating_supply Circulating supply of the coin.
 * @property total_supply Total supply of the coin.
 * @property max_supply Maximum possible supply of the coin.
 * @property percent_change_24h Percentage change in price in the past 24 hours.
 * @property infinite_supply Flag indicating whether the coin has infinite supply.
 * @property last_updated Last update time of the coin's data.
 * @property date_added The date when the coin was added.
 * @property tags List of tags associated with the coin.
 * @property quote Map of quotes for the coin for each currency.
 */
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

/**
 * Data class representing a coin's quote data in a specific currency.
 *
 * @property price Current price of the coin.
 * @property volume_24h 24 hour trade volume for the coin.
 * @property volume_change_24h 24 hour volume change for the coin.
 * @property percent_change_24h 24 hour percentage price change for the coin.
 * @property market_cap Market capitalization of the coin.
 * @property fully_diluted_market_cap Fully diluted market cap for the coin.
 * @property market_cap_dominance Market Capitalization dominance of the coin.
 * @property last_updated Last updated time of the data.
 */
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
/**
 *
 * Extension function on [CoinData] data class that converts it to [CoinEntity] object.
 *
 * This function is used for converting the [CoinData] object (that is used for network responses)
 * to [CoinEntity] object (that is used for storing data in local database).
 *
 * @return [CoinEntity] object constructed with properties of the receiver [CoinData] object.
 */
fun CoinData.asDatabaseEntity(): CoinEntity {
    val formatter = DecimalFormat("#.##")
    formatter.isGroupingUsed = false // Avoids inserting commas in large numbers

    return CoinEntity(
        id = id,
        name = name,
        symbol = symbol,
        rank = cmc_rank,
        price = formatter.format(quote["USD"]?.price ?: 0.0).toDouble(),
        percentChange24h = formatter.format(quote["USD"]?.percent_change_24h ?: 0.0).toDouble(),
        marketCap = formatter.format(quote["USD"]?.market_cap ?: 0.0).toDouble(),
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
            volume = formatter.format(quote["USD"]?.volume_24h ?: 0.0).toDouble(),
            fullyDilutedMarketCap = formatter.format(quote["USD"]?.fully_diluted_market_cap ?: 0.0)
                .toDouble(),
            marketCapDominance = formatter.format(quote["USD"]?.market_cap_dominance ?: 0.0)
                .toDouble(),
            quoteLastUpdated = quote["USD"]?.last_updated ?: ""
        )
    )
}



