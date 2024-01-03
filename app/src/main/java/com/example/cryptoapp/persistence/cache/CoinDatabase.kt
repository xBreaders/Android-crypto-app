package com.example.cryptoapp.persistence.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Abstract class to access database using Room.
 *
 * This class must be abstract and extend RoomDatabase. You define the DAOs that work with the database within the abstract class.
 * Since it's a singleton, it will have a single instance across the whole application.
 */
@Database(entities = [CoinEntity::class], version = 2, exportSchema = false)
abstract class CoinDatabase : RoomDatabase() {
    /**
     * Abstract method with no parameters that you use to access the [CoinDao].
     *
     * @return The DAO object that serves as a handle to access the saved data.
     */
    abstract fun coinDao(): CoinDao

    companion object {
        /**
         * Singleton instance for CoinDatabase.
         */
        @Volatile
        private var INSTANCE: CoinDatabase? = null

        /**
         * Method for getting the database. This is a singleton to prevent multiple instances of the database from being opened at the same time.
         * Also this has a fallbackToDestructiveMigration to handle version changes.
         *
         * @param context Context to build the database
         * @return [CoinDatabase] instance.
         */
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
