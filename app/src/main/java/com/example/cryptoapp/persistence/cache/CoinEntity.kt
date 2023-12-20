package com.example.cryptoapp.persistence.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cryptocurrencies")
data class CoinEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val symbol: String,
    val rank: Int,
    val price: Double,
    val volume24h: Double,
    val percentChange24h: Double,
    val marketCap: Double
)