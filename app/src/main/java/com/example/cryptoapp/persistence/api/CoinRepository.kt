package com.example.cryptoapp.persistence.api

import com.example.cryptoapp.persistence.api.response.CryptoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Interface representing a CoinMarketCap API client.
 *
 * This interface provides methods to interact with the CoinMarketCap API which allows you to fetch
 * Cryptocurrency data.
 */
interface CoinMarketCapService {

    /**
     * This function fetches the latest cryptocurrency listings.
     *
     * @param start The position in the list to start the data output.
     * @param limit The number of crypto listings to return.
     * @param convert The currency to get the crypto listings in.
     * @return Retrofit [Response] object containing [CryptoResponse].
     */
    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getCryptoListings(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 200,
        @Query("convert") convert: String = "USD"
    ): Response<CryptoResponse>

    /**
     * This method fetches a specific coin by its symbol.
     *
     * @param symbol The symbol of the coin to fetch.
     * @param convert The currency to get the coin data in.
     * @return Retrofit [Response] object containing [CryptoResponse].
     */
    @GET("v1/cryptocurrency/listings/latest")
    suspend fun requestCoinById(
        @Query("symbol") symbol: String,
        @Query("convert") convert: String = "USD"
    ): Response<CryptoResponse>

    /**
     * Fetches K-line data for the given symbol from a different API (Possibly Binance) as this feature
     * is not available freely in CoinMarketCap's API.
     *
     * @param url The complete API url to fetch the KLines for the symbol.
     * @param symbol The symbol to fetch K-line data for.
     * @param interval The interval of K-lines.
     * @param limit The maximum number of K-lines to fetch.
     * @return Retrofit [Response] object containing a List of String lists representing K-line data.
     */
    @GET
    suspend fun getKLinesBySymbol(
        @Url url: String,
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("limit") limit: Int = 30,
    ): Response<List<List<String>>>
}