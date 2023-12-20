package com.example.cryptoapp.persistence.API


import retrofit2.Response

interface DefaultCoinRepository {

    suspend fun getCryptoMap(): Response<CryptoResponse>

    suspend fun getCryptoListings(): Response<CryptoResponse>
}

class DefaultCoinRepositoryImpl(private val coinMarketCapService: CoinMarketCapService) :
    DefaultCoinRepository {

    override suspend fun getCryptoMap(): Response<CryptoResponse> {
        return coinMarketCapService.getCryptoMap()
    }

    override suspend fun getCryptoListings(): Response<CryptoResponse> {
        return coinMarketCapService.getCryptoListings()
    }
}