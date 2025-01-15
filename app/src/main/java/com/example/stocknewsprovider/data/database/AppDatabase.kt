package com.example.stocknewsprovider.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stocknewsprovider.data.dao.StockDao
import com.example.stocknewsprovider.data.entity.StockEntity

@Database(entities = [StockEntity::class], version = 2)  // 버전 2로 업데이트
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
                )
                    .fallbackToDestructiveMigration()  // 기존 데이터 삭제하고 새로 시작
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}