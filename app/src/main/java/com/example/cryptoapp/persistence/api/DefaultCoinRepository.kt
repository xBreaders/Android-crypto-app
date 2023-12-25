package com.example.cryptoapp.persistence.api


import com.example.cryptoapp.persistence.cache.CoinDao
import com.example.cryptoapp.persistence.cache.CoinEntity
import com.example.cryptoapp.persistence.cache.asDetailedDomainObject
import com.example.cryptoapp.persistence.cache.asDomainObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.sql.SQLException

interface DefaultCoinRepository {

    suspend fun getCryptoMap(): CryptoResponse

    suspend fun getCryptoListings(): Response<CryptoResponse>

    fun getCoinById(id: Int): Flow<CoinData>

    suspend fun getAllCoins(): Flow<List<CoinData>>

    suspend fun upsertCoins(coinList: List<CoinEntity>)


}

class DefaultCoinRepositoryImpl(
    private val coinMarketCapService: CoinMarketCapService,
    private val coinDao: CoinDao
) :
    DefaultCoinRepository {

    override suspend fun getCryptoMap(): CryptoResponse {
        return coinMarketCapService.getCryptoMap()
    }

    override suspend fun getCryptoListings(): Response<CryptoResponse> {
        return coinMarketCapService.getCryptoListings()
    }

    override fun getCoinById(id: Int): Flow<CoinData> {
        try {
            return coinDao.getCoinById(id).map { it.asDetailedDomainObject() }
        } catch (e: SQLException) {
            throw Exception("Error retrieving coin from database: ${e.message}")
        } catch (e: Exception) {
            throw Exception("Unknown error retrieving coin from database: ${e.message}")
        }
    }

    override suspend fun getAllCoins(): Flow<List<CoinData>> {
        try {
            return coinDao.getAllCoins().map { it.asDomainObject() }
        } catch (e: SQLException) {
            throw Exception("Error retrieving coin list from database: ${e.message}")
        } catch (e: Exception) {
            throw Exception("Unknown error retrieving coin list from database: ${e.message}")
        }
    }

    override suspend fun upsertCoins(coinList: List<CoinEntity>) {
        try {
            coinDao.upsertCoins(coinList)
        } catch (e: SQLException) {
            throw Exception("Error inserting coin list into database: ${e.message}")
        } catch (e: Exception) {
            throw Exception("Unknown error inserting coin list into database: ${e.message}")
        }
    }
}