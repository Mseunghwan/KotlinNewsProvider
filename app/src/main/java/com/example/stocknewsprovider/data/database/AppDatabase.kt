// 4. data/database/AppDatabase.kt
package com.example.stocknewsprovider.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stocknewsprovider.data.entity.StockEntity
import com.example.stocknewsprovider.data.dao.StockDao

@Database(entities = [StockEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "stock_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}