package com.example.cryptoapp.persistence.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Query("SELECT * FROM cryptocurrencies ORDER BY rank LIMIT 5000")
    suspend fun getAllCryptos(): Flow<List<CoinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cryptos: List<CoinEntity>)

    @Query("DELETE FROM cryptocurrencies")
    suspend fun deleteAll()
}