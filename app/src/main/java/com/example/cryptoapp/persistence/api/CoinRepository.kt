package com.example.cryptoapp.persistence.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CoinMarketCapService {

    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getCryptoListings(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 200,
        @Query("convert") convert: String = "USD"
    ): Response<CryptoResponse>

    @GET
    suspend fun getKLinesBySymbol(
        //using different api for klines, because the other one is paid
        @Url url: String,
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("limit") limit: Int = 30,
    ): Response<List<List<String>>>

}
