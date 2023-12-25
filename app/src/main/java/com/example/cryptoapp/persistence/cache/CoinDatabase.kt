package com.example.cryptoapp.persistence.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CoinEntity::class], version = 2)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao

    // Singleton instance
    companion object {
        @Volatile
        private var INSTANCE: CoinDatabase? = null

        fun getDatabase(context: Context): CoinDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    CoinDatabase::class.java,
                    "crypto_db",
                ).fallbackToDestructiveMigration() // destructive migratie bij versie update
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
