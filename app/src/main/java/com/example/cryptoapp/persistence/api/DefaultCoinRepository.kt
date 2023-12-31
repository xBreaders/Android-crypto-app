package com.example.cryptoapp.persistence.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.cryptoapp.persistence.cache.CoinDao
import com.example.cryptoapp.persistence.cache.CoinEntity
import com.example.cryptoapp.persistence.cache.asDetailedDomainObject
import com.example.cryptoapp.persistence.cache.asDomainObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.sql.SQLException

/**
 * Interface for accessing coin data.
 */
interface DefaultCoinRepository {

    /**
     * Fetches a list of cryptocurrencies from the API.
     * @return [Response] containing [CryptoResponse].
     * @throws Exception if there is a network or server error.
     */
    suspend fun getCryptoListings(): Response<CryptoResponse>

    /**
     * Retrieves details of a specific coin by its ID.
     * @param id The unique identifier of the coin.
     * @return A [Flow] emitting [CoinData] for the specified coin.
     * @throws Exception if there is an error accessing the database.
     */
    fun getCoinById(id: Int): Flow<CoinData>

    /**
     * Fetches all coins from the local database.
     * @return A [Flow] emitting a list of [CoinData].
     * @throws Exception if there is an error accessing the database.
     */
    suspend fun getAllCoins(): Flow<List<CoinData>>

    /**
     * Inserts or updates a list of coins in the local database.
     * @param coinList The list of [CoinEntity] to be upserted.
     * @throws Exception if there is an error updating the database.
     */
    suspend fun upsertCoins(coinList: List<CoinEntity>)

    /**
     * Retrieves a paginated list of coins.
     * @return A [Flow] emitting [PagingData] containing [CoinData].
     */
    fun getPagedCoins(): Flow<PagingData<CoinData>>

    suspend fun getKLinesBySymbol(
        symbol: String,
        interval: String
    ): List<KLine>


}

/**
 * Implementation of [DefaultCoinRepository] that retrieves cryptocurrency data
 * from a remote service and a local database.
 *
 * @property coinMarketCapService The service for accessing remote cryptocurrency data.
 * @property coinDao Local database that stores cryptocurrency data.
 */
class DefaultCoinRepositoryImpl(
    private val coinMarketCapService: CoinMarketCapService,
    private val coinDao: CoinDao
) :
    DefaultCoinRepository {
    /**
     * Retrieves a list of cryptocurrencies from the remote service.
     * @return [Response] containing [CryptoResponse].
     * @throws SQLException if there is a database access error.
     * @throws Exception for other errors.
     */
    override suspend fun getCryptoListings(): Response<CryptoResponse> {
        return coinMarketCapService.getCryptoListings()
    }

    /**
     * Retrieves details of a specific coin by its ID from the local database.
     * @param id The unique identifier of the coin.
     * @return A [Flow] emitting [CoinData] for the specified coin.
     * @throws SQLException if there is a database access error.
     * @throws Exception for other errors.
     */
    override fun getCoinById(id: Int): Flow<CoinData> {
        try {
            return coinDao.getCoinById(id).map { it.asDetailedDomainObject() }
        } catch (e: SQLException) {
            throw Exception("Error retrieving coin from database: ${e.message}")
        } catch (e: Exception) {
            throw Exception("Unknown error retrieving coin from database: ${e.message}")
        }
    }

    /**
     * Fetches all coins from the local database.
     * @return A [Flow] emitting a list of [CoinData].
     * @throws SQLException if there is a database access error.
     * @throws Exception for other errors.
     */
    override suspend fun getAllCoins(): Flow<List<CoinData>> {
        try {
            return coinDao.getAllCoins().map { it.asDomainObject() }
        } catch (e: SQLException) {
            throw Exception("Error retrieving coin list from database: ${e.message}")
        } catch (e: Exception) {
            throw Exception("Unknown error retrieving coin list from database: ${e.message}")
        }
    }

    /**
     * Inserts or updates a list of coins in the local database.
     * @param coinList The list of [CoinEntity] to be upserted.
     * @throws SQLException if there is a database access error.
     * @throws Exception for other errors.
     */
    override suspend fun upsertCoins(coinList: List<CoinEntity>) {
        try {
            coinDao.upsertCoins(coinList)
        } catch (e: SQLException) {
            throw Exception("Error inserting coin list into database: ${e.message}")
        } catch (e: Exception) {
            throw Exception("Unknown error inserting coin list into database: ${e.message}")
        }
    }

    /**
     * Retrieves a paginated list of coins from the local database.
     * @return A [Flow] emitting [PagingData] containing [CoinData].
     */
    override fun getPagedCoins(): Flow<PagingData<CoinData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,  // Define the number of items loaded at once from the PagingSource
                enablePlaceholders = true,
                maxSize = 60
            ),
            pagingSourceFactory = { coinDao.getPagedCoins() }
        ).flow.map { pagingData -> pagingData.map { it.asDomainObject() } }
    }

    override suspend fun getKLinesBySymbol(
        symbol: String,
        interval: String
    ): List<KLine> {
        val response = coinMarketCapService.getKLinesBySymbol(
            url = "https://api.binance.com/api/v3/klines",
            symbol = symbol,
            interval = interval
        )

        return if (response.isSuccessful) {
            response.body()?.map { convertToKLine(it) } ?: emptyList()
        } else {
            throw Exception("Error retrieving klines: ${response.message()}")
        }

    }

    private fun convertToKLine(rawList: List<String>): KLine {
        return KLine(
            openTime = rawList[0].toLong(),
            open = rawList[1].toDouble(),
            high = rawList[2].toDouble(),
            low = rawList[3].toDouble(),
            close = rawList[4].toDouble(),
            volume = rawList[5].toDouble(),
            closeTime = rawList[6].toLong(),
            quoteAssetVolume = rawList[7].toDouble(),
            numberOfTrades = rawList[8].toInt(),
            takerBuyBaseAssetVolume = rawList[9].toDouble(),
            takerBuyQuoteAssetVolume = rawList[10].toDouble(),
        )
    }


}