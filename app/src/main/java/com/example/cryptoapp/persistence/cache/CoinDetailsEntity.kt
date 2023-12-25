package com.example.cryptoapp.persistence.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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


