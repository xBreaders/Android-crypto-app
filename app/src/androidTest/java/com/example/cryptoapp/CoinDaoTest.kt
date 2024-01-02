package com.example.cryptoapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.cryptoapp.persistence.cache.CoinDao
import com.example.cryptoapp.persistence.cache.CoinDatabase
import com.example.cryptoapp.persistence.cache.CoinDetailsEntity
import com.example.cryptoapp.persistence.cache.CoinEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CoinDaoTest {

    private lateinit var database: CoinDatabase
    private lateinit var coinDao: CoinDao


    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CoinDatabase::class.java
        ).allowMainThreadQueries().build()
        coinDao = database.coinDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testInsertCoin() = runTest(UnconfinedTestDispatcher()) {
        val coin = CoinEntity(
            id = 1,
            name = "Bitcoin",
            symbol = "BTC",
            rank = 1,
            price = 10000.0,
            percentChange24h = 0.0,
            marketCap = 100000000.0,
            coinDetails = CoinDetailsEntity(
                coinId = 1,
                circulatingSupply = 1000000.0,
                totalSupply = 1000000.0,
                maxSupply = 1000000.0,
                numMarketPairs = 1,
                lastUpdated = "2021-01-01",
                dateAdded = "2021-01-01",
                tags = "tag1,tag2",
                slug = "bitcoin",
                infiniteSupply = false,
                volume = 1000000.0,
                fullyDilutedMarketCap = 1000000.0,
                marketCapDominance = 1000000.0,
                quoteLastUpdated = "2021-01-01",
            )
        )



        coinDao.upsertCoins(listOf(coin))
        val coinList = coinDao.getAllCoins().firstOrNull()
        assert(coinList!!.contains(coin))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searching for existing coin returns correct coin`() = runTest(UnconfinedTestDispatcher()) {
        val coin = CoinEntity(
            id = 1,
            name = "Bitcoin",
            symbol = "BTC",
            rank = 1,
            price = 10000.0,
            percentChange24h = 0.0,
            marketCap = 100000000.0,
            coinDetails = CoinDetailsEntity(
                coinId = 1,
                circulatingSupply = 1000000.0,
                totalSupply = 1000000.0,
                maxSupply = 1000000.0,
                numMarketPairs = 1,
                lastUpdated = "2021-01-01",
                dateAdded = "2021-01-01",
                tags = "tag1,tag2",
                slug = "bitcoin",
                infiniteSupply = false,
                volume = 1000000.0,
                fullyDilutedMarketCap = 1000000.0,
                marketCapDominance = 1000000.0,
                quoteLastUpdated = "2021-01-01",
            )
        )



        coinDao.upsertCoins(listOf(coin))
        val searchResult = coinDao.searchCoins("Bitcoin").firstOrNull()
        assert(searchResult!!.any { it.name == "Bitcoin" })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searching for non existing coin returns empty list`() =
        runTest(UnconfinedTestDispatcher()) {
            val searchResult = coinDao.searchCoins("NonExistingCoin").firstOrNull()
            assert(searchResult!!.isEmpty())
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getting coin by id returns correct coin`() = runTest(UnconfinedTestDispatcher()) {
        val coin = CoinEntity(
            id = 1,
            name = "Bitcoin",
            symbol = "BTC",
            rank = 1,
            price = 10000.0,
            percentChange24h = 0.0,
            marketCap = 100000000.0,
            coinDetails = CoinDetailsEntity(
                coinId = 1,
                circulatingSupply = 1000000.0,
                totalSupply = 1000000.0,
                maxSupply = 1000000.0,
                numMarketPairs = 1,
                lastUpdated = "2021-01-01",
                dateAdded = "2021-01-01",
                tags = "tag1,tag2",
                slug = "bitcoin",
                infiniteSupply = false,
                volume = 1000000.0,
                fullyDilutedMarketCap = 1000000.0,
                marketCapDominance = 1000000.0,
                quoteLastUpdated = "2021-01-01",
            )
        )

        coinDao.upsertCoins(listOf(coin))

        val coinResult = coinDao.getCoinById(1).firstOrNull()
        assert(coinResult!!.id == 1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getting coin by non existing id returns null`() = runTest(UnconfinedTestDispatcher()) {
        val coin = coinDao.getCoinById(9999).firstOrNull()
        assert(coin == null)
    }


}