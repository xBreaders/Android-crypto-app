package com.example.cryptoapp.persistence.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a detailed coin entity in the database.
 *
 * Each instance represents a row in a database table.
 * This class also defines the table's name "coin_details" and its columns.
 *
 * @property coinId The primary key. It is a unique identifier for a coin.
 * @property circulatingSupply The amount of coin that is in circulation.
 * @property totalSupply The total supply of coin.
 * @property maxSupply The maximum supply of coin. Nullable if the max supply is not determined.
 * @property numMarketPairs The number of market pairs.
 * @property lastUpdated The date of the last update.
 * @property dateAdded The date when the coin was added.
 * @property tags The tags of the coin, stored as a comma-separated string.
 * @property slug The slug of the coin.
 * @property infiniteSupply A Boolean indicating whether the coin has an infinite supply.
 * @property volume The trading volume of the coin.
 * @property fullyDilutedMarketCap The fully diluted market cap of the coin.
 * @property marketCapDominance The market cap dominance of the coin.
 * @property quoteLastUpdated The last update of the quote.
 */
@Entity(tableName = "coin_details")
data class CoinDetailsEntity(
    @PrimaryKey
    @ColumnInfo(name = "coin_id")
    val coinId: Int,
    val circulatingSupply: Double,
    val totalSupply: Double,
    val maxSupply: Double?,
    val numMarketPairs: Int,
    val lastUpdated: String,
    val dateAdded: String,
    val tags: String, // Storing as a comma-separated string
    val slug: String,
    val infiniteSupply: Boolean,
    val volume: Double,
    val fullyDilutedMarketCap: Double,
    val marketCapDominance: Double,
    val quoteLastUpdated: String,
)


