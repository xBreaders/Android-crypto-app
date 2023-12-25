package com.example.cryptoapp.persistence.cache

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Query("SELECT * FROM cryptocurrencies ORDER BY rank LIMIT 5000")
    fun getAllCoins(): Flow<List<CoinEntity>>

    @Upsert
    suspend fun upsertCoins(coins: List<CoinEntity>)

    @Query("SELECT * FROM cryptocurrencies WHERE id = :coinId")
    fun getCoinById(coinId: Int): Flow<CoinEntity>


}