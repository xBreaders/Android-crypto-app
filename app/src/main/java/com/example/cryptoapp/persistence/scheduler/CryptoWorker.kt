package com.example.cryptoapp.persistence.scheduler

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cryptoapp.CoinApp
import com.example.cryptoapp.persistence.api.asDatabaseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A specialized class that performs background tasks using coroutines.
 *
 * Create a Worker when you need to perform CPU-intensive work outside of the main thread.
 * The input and output data for a Worker is a set of key/value pairs, stored in a Data object.
 *
 * @param appContext The application context
 * @param workerParams Parameters to setup the internal state of this worker
 */
class CryptoWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val app = applicationContext as CoinApp
    private val repository = app.container.repository

    /**
     * The task to run on the background thread. The method is marked with the suspend modifier
     * because it performs a potentially time-consuming operation and does not block the current thread.
     *
     * @return The result of the background work, represented by a [Result] object.
     */
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        // Perform the data fetch
        val response = repository.getCryptoListings()
        response.data.map { it.asDatabaseEntity() }.let {
            repository.upsertCoins(it)
        }
        Result.success()
    }
}


