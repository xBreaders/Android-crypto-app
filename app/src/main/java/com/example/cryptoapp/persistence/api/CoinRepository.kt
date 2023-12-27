package com.example.cryptoapp.persistence.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinMarketCapService {

    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getCryptoListings(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 200,
        @Query("convert") convert: String = "USD"
    ): Response<CryptoResponse>

}
