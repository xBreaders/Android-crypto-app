package com.example.cryptoapp.persistence.API

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinMarketCapService {
    @GET("v1/cryptocurrency/map")
    suspend fun getCryptoMap(): Response<CryptoResponse>

    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getCryptoListings(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 5000,
        @Query("convert") convert: String = "USD"
    ): Response<CryptoResponse>

}
