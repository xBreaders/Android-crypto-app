package com.example.cryptoapp.persistence.scheduler

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cryptoapp.persistence.API.DefaultCoinRepository
import com.example.cryptoapp.persistence.cache.CoinDatabase

class CryptoWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val repository: DefaultCoinRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Perform the data fetch
            val response = repository.getCryptoListings()
            if (response.isSuccessful) {
                // Map the network model to database entity and cache it
                response.body()?.data?.map { it.toEntity() }?.let {
                    val dao = CoinDatabase.getDatabase(applicationContext).cryptoDao()
                    dao.deleteAll()
                    dao.insertAll(it)
                }
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}


