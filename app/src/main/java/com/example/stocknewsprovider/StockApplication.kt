package com.example.stocknewsprovider

import android.app.Application
import android.util.Log
import com.example.stocknewsprovider.data.database.AppDatabase
import com.example.stocknewsprovider.data.entity.StockEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class StockApplication : Application() {
    private val TAG = "StockApplication"
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // 데이터베이스 초기화를 lazy로 변경
    private val database by lazy {
        Log.d(TAG, "Initializing database")
        AppDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "StockApplication onCreate")
        initializeData()
    }

    private fun initializeData() {
        applicationScope.launch {
            try {
                val stockCount = database.stockDao().getStockCount()
                Log.d(TAG, "Current stock count: $stockCount")

                if (stockCount == 0) {
                    Log.d(TAG, "Database is empty, loading initial data")
                    loadInitialData()
                } else {
                    Log.d(TAG, "Database already contains data")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error initializing database", e)
            }
        }
    }

    private suspend fun loadInitialData() {
        try {
            val stockDao = database.stockDao()

            val stocks = listOf(
                StockEntity("005930", "삼성전자", "KOSPI", "전자", "전자제품 제조"),
                StockEntity("000660", "SK하이닉스", "KOSPI", "반도체", "반도체 제조"),
                StockEntity("035420", "NAVER", "KOSPI", "서비스", "인터넷 서비스"),
                StockEntity("005380", "현대차", "KOSPI", "자동차", "자동차 제조"),
                StockEntity("051910", "LG화학", "KOSPI", "화학", "화학제품 제조"),
                // NASDAQ 주요 종목 추가
                StockEntity("AAPL", "Apple Inc.", "NASDAQ", "Technology", "Consumer Electronics"),
                StockEntity("MSFT", "Microsoft Corporation", "NASDAQ", "Technology", "Software"),
                StockEntity("GOOGL", "Alphabet Inc.", "NASDAQ", "Technology", "Internet Services"),
                // NYSE 주요 종목 추가
                StockEntity("JPM", "JPMorgan Chase", "NYSE", "Financial", "Banking"),
                StockEntity("JNJ", "Johnson & Johnson", "NYSE", "Healthcare", "Pharmaceuticals")
            )

            stockDao.insertAll(stocks)
            Log.d(TAG, "Successfully inserted ${stocks.size} stocks")

            // 데이터 검증
            val finalCount = stockDao.getStockCount()
            Log.d(TAG, "Final stock count: $finalCount")

        } catch (e: Exception) {
            Log.e(TAG, "Error loading initial data", e)
            throw e
        }
    }

    companion object {
        private lateinit var instance: StockApplication

        fun getInstance(): StockApplication {
            return instance
        }
    }

    init {
        instance = this
    }
}