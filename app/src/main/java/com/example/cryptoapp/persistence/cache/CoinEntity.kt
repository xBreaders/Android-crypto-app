package com.example.cryptoapp.persistence.cache

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cryptoapp.persistence.api.CoinData
import com.example.cryptoapp.persistence.api.CryptoQuote

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
    val coinDetails: CoinDetailsEntity
)


fun CoinEntity.asDetailedDomainObject(): CoinData {
    return CoinData(
        id = id,
        name = name,
        symbol = symbol,
        cmc_rank = rank,

        //data class CoinData(
        //    val id: Int = 0,
        //    val name: String = "",
        //    val symbol: String = "",
        //    val slug: String = "",
        //    val cmc_rank: Int = 0,
        //    val num_market_pairs: Int = 0,
        //    val circulating_supply: Double = 0.0,
        //    val total_supply: Double = 0.0,
        //    val max_supply: Double? = null,
        //    val infinite_supply: Boolean = false,
        //    val last_updated: String = "",
        //    val date_added: String = "",
        //    val tags: List<String> = listOf(),
        //    @SerializedName("quote")
        //    val quote: Map<String, CryptoQuote> = mapOf()
        //)
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
                //data class CryptoQuote(
                //    val price: Double = 0.0,
                //    val volume_24h: Double = 0.0,
                //    val volume_change_24h: Double = 0.0,
                //    val market_cap: Double = 0.0,
                //    val fully_diluted_market_cap: Double = 0.0,
                //    val market_cap_dominance: Double = 0.0,
                //    val last_updated: String = ""
                //)
            )
        ),
    )
}

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

fun List<CoinEntity>.asDomainObject(): List<CoinData> {
    return map {
        it.asDomainObject()
    }
}