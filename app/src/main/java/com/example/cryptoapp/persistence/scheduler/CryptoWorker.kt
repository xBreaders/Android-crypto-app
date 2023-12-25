package com.example.cryptoapp.persistence.scheduler

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cryptoapp.persistence.api.DefaultApp
import com.example.cryptoapp.persistence.api.asDatabaseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CryptoWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val repository = DefaultApp(appContext).repository

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        // Perform the data fetch
        val response = repository.getCryptoListings()
        if (response.isSuccessful) {
            // Map the network model to database entity and cache it
            response.body()?.data?.map { it.asDatabaseEntity() }?.let {
                repository.upsertCoins(it)
            }
            Result.success()
        } else {
            Result.retry()
        }
    }

}


