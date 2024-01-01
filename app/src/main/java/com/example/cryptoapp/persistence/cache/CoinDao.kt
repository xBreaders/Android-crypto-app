package com.example.cryptoapp.persistence.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the coins table in the CoinApp.
 * Since this interface is marked with @Dao, Room will generate an implementation for it.
 */
@Dao
interface CoinDao {

    /**
     * Returns Flow of all coins from the database ordered by rank and limited to 5000.
     *
     * @return Flow<List<CoinEntity>> list of all coins.
     */
    @Query("SELECT * FROM cryptocurrencies ORDER BY rank LIMIT 5000")
    fun getAllCoins(): Flow<List<CoinEntity>>

    /**
     * Returns a PagingSource of all coins from the database ordered by rank.
     *
     * @return PagingSource<Int, CoinEntity> A data loaded in pages.
     */
    @Query("SELECT * FROM cryptocurrencies ORDER BY RANK")
    fun getPagedCoins(): PagingSource<Int, CoinEntity>

    /**
     * Inserts a list of coins into the database, on conflict (if the list already exist), it will be replaced.
     *
     * @param coins Coins to insert or update
     */
    @Upsert
    suspend fun upsertCoins(coins: List<CoinEntity>)

    /**
     * Returns the coin with the specific id from the database.
     *
     * @param coinId The id of the coin.
     * @return Flow<CoinEntity> the requested coin.
     */
    @Query("SELECT * FROM cryptocurrencies WHERE id = :coinId")
    fun getCoinById(coinId: Int): Flow<CoinEntity>

    /**
     * Returns the coins that match the provided query.
     *
     * @param query The string query.
     * @return Flow<List<CoinEntity>> the coins that match the query.
     */
    @Query("SELECT * FROM cryptocurrencies WHERE name LIKE :query OR symbol LIKE :query OR id LIKE :query")
    fun searchCoins(query: String): Flow<List<CoinEntity>>

}