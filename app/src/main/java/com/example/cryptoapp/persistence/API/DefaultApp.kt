package com.example.cryptoapp.persistence.API

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("X-CMC_PRO_API_KEY", apiKey)
            .build()
        return chain.proceed(newRequest)
    }
}

interface AppContainer {
    val repository: DefaultCoinRepository
}


class DefaultApp : AppContainer {
    private val baseUrl = "https://pro-api.coinmarketcap.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor("b1888a64-a5ef-45be-88aa-c9567748c0e9")) // Replace with your actual API key
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(
            GsonConverterFactory.create(),
        )
        .build()

    private val retrofitService: CoinMarketCapService by lazy {
        retrofit.create(CoinMarketCapService::class.java)

    }

    override val repository: DefaultCoinRepository by lazy {
        DefaultCoinRepositoryImpl(retrofitService)
    }
}


