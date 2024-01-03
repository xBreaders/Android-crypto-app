package com.example.cryptoapp.persistence.cache

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cryptoapp.persistence.api.response.CoinData
import com.example.cryptoapp.persistence.api.response.CryptoQuote

/**
 * Data class representing a coin entity in the database.
 *
 * Each instance represents a row in a database table.
 * This class also defines the table's name "cryptocurrencies" and its columns.
 *
 * @property id The primary key. It is a unique identifier for a coin.
 * @property name The name of the coin.
 * @property symbol The symbol of the coin.
 * @property rank The rank of the coin in cryptocurrencies.
 * @property price The price of the coin.
 * @property percentChange24h The percentage change in the last 24 hours.
 * @property marketCap The market capitalization of the coin.
 * @property coinDetails The embedded object that holds the details of a coin.
 */
@Entity(tableName = "cryptocurrencies")
data class CoinEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "rank")
    val rank: Int,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "percentChange24h")
    val percentChange24h: Double,
    @ColumnInfo(name = "marketCap")
    val marketCap: Double,
    @Embedded(prefix = "details_")
    val coinDetails: CoinDetailsEntity,
)

/**
 * Method to map the database object to the domain object with all coin details.
 *
 * @return [CoinData] Domain object with all details of the coin.
 */
fun CoinEntity.asDetailedDomainObject(): CoinData {
    return CoinData(
        id = id,
        name = name,
        symbol = symbol,
        cmc_rank = rank,
        slug = coinDetails.slug,
        circulating_supply = coinDetails.circulatingSupply,
        total_supply = coinDetails.totalSupply,
        max_supply = coinDetails.maxSupply,
        infinite_supply = coinDetails.infiniteSupply,
        last_updated = coinDetails.lastUpdated,
        date_added = coinDetails.dateAdded,
        tags = coinDetails.tags.split(","),
        num_market_pairs = coinDetails.numMarketPairs,
        quote = mapOf(
            "USD" to CryptoQuote(
                price = price,
                percent_change_24h = percentChange24h,
                market_cap = marketCap,
                fully_diluted_market_cap = coinDetails.fullyDilutedMarketCap,
                market_cap_dominance = coinDetails.marketCapDominance,
                last_updated = coinDetails.quoteLastUpdated,
                volume_24h = coinDetails.volume,
            )
        ),
    )
}

/**
 * Method to map the database object to the domain object with only the essential details.
 *
 * @return [CoinData] Domain object with essential details of the coin.
 */
fun CoinEntity.asDomainObject(): CoinData {
    return CoinData(
        id = id,
        name = name,
        symbol = symbol,
        cmc_rank = rank,
        quote = mapOf(
            "USD" to CryptoQuote(
                price = price,
                percent_change_24h = percentChange24h
            )
        )
    )
}

/**
 * Method to map a list of database objects to the domain object with only the essential details.
 *
 * @return [List<CoinData>] list of Domain objects with essential details of the coin.
 */
fun List<CoinEntity>.asDomainObject(): List<CoinData> {
    return map {
        it.asDomainObject()
    }
}

