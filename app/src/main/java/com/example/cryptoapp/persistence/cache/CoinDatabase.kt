package com.example.cryptoapp.persistence.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CoinEntity::class], version = 1, exportSchema = false)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun cryptoDao(): CoinDao

    // Singleton instance
    companion object {
        @Volatile
        private var instance: CoinDatabase? = null

        fun getDatabase(context: Context): CoinDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context): CoinDatabase {
            return Room.databaseBuilder(context, CoinDatabase::class.java, "crypto-db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
