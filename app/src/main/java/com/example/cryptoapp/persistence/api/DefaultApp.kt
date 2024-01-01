package com.example.cryptoapp.persistence.api

import android.content.Context
import com.example.cryptoapp.persistence.cache.CoinDatabase
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * The `ApiKeyInterceptor` class is a custom defined interceptor to append the API key to requests that are made with the OkHttpClient.
 * The API key is required by the web service to authenticate requests originating from this application.
 *
 * @constructor takes an API key as a parameter which is a String and uses it to create an instance of the class.
 *
 * @property apiKey String value used for accessing the web service. It is passed when creating a new instance of the class.
 *
 * The class implements the `Interceptor` interface and overrides the `intercept` function.
 *
 * The `intercept` function intercepts the outgoing request and adds the API key in its header. It does so by obtaining the original request from
 * the chain, building a new request with an additional API key header, and continuing the chain call with this new request.
 * Finally, it returns the response received from the chain.
 *
 * @param chain The chain of request/response interactions. It allows us to retrieve the original request and use it's builder to add a header.
 *
 * @function intercept Implement this function to add API key to every request's header before it is sent to the server.
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
 * `AppContainer` is the interface that defines the application's dependencies container for Dependency Injection purposes.
 * This is where we inject all the dependencies required by the application which promotes loose coupling and easier unit
 * testing. It acts as the central part of the Dependency Injection system.
 *
 * @property repository An instance of DefaultCoinRepository. This is your way to access the functionality
 * provided by the data layer, i.e., fetching, storing, and managing cryptocurrency data.
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
class DefaultApp(private val context: Context, private val sharedVM: SharedViewModel) :
    AppContainer {

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
            CoinDatabase.getDatabase(context = context).coinDao(),
            sharedVM
        )
    }
}



