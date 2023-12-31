package com.example.cryptoapp.persistence.api

import android.content.Context
import com.example.cryptoapp.persistence.cache.CoinDatabase
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * An interceptor for appending the API key to requests made through OkHttpClient.
 *
 * @property apiKey The API key for accessing the web service.
 */
class ApiKeyInterceptor(private val apiKey: String) : Interceptor {

    /**
     * Intercepts an outgoing request and adds the API key in its header.
     *
     * @param chain The chain of request/response interactions.
     * @return The response from the chain.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("X-CMC_PRO_API_KEY", apiKey)
            .build()
        return chain.proceed(newRequest)
    }
}


/**
 * Defines the container for the application's dependencies.
 * This interface is used for dependency injection.
 */
interface AppContainer {

    /**
     * The repository for accessing cryptocurrency data.
     */
    val repository: DefaultCoinRepository
}



/**
 * The default implementation of [AppContainer], providing dependency injection for the app.
 *
 * @property context The application context used for database and network setup.
 */
class DefaultApp(private val context: Context) : AppContainer {

    private val baseUrl = "https://pro-api.coinmarketcap.com/"

    /**
     * OkHttpClient configured with an interceptor for API key inclusion.
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor("b1888a64-a5ef-45be-88aa-c9567748c0e9")) // Replace with your actual API key
        .build()


    /**
     * Retrofit service for making network requests.
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Lazy initialization of the CoinMarketCapService.
     */
    private val retrofitService: CoinMarketCapService by lazy {
        retrofit.create(CoinMarketCapService::class.java)
    }

    /**
     * Provides a singleton instance of [DefaultCoinRepository].
     */
    override val repository: DefaultCoinRepository by lazy {
        DefaultCoinRepositoryImpl(
            retrofitService,
            CoinDatabase.getDatabase(context = context).coinDao()
        )
    }
}



