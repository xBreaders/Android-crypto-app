package com.example.cryptoapp.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: CoinMarketCapService by lazy {
        Retrofit.Builder()
            .baseUrl("https://pro-api.coinmarketcap.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinMarketCapService::class.java)
    }
}