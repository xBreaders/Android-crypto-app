package com.example.cryptoapp.API

import retrofit2.Response
import retrofit2.http.GET

interface CoinMarketCapService {
    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getListOfCryptos(): Response<List<CryptoEntity>>
}