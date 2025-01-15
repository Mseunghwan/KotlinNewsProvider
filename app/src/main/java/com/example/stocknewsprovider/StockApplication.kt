package com.example.stocknewsprovider

import android.app.Application
import android.util.Log
import com.example.stocknewsprovider.data.database.AppDatabase
import com.example.stocknewsprovider.data.entity.StockEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class StockApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.IO)
    val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            loadInitialData()
        }
    }

    private suspend fun loadInitialData() {
        val stockDao = database.stockDao()
        if (stockDao.getStockCount() == 0) {
            val markets = listOf("KOSPI", "NASDAQ", "NYSE")
            markets.forEach { market ->
                try {
                    assets.open("$market.csv").use { inputStream ->
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        // 헤더 건너뛰기
                        reader.readLine()

                        val stocks = reader.lineSequence()
                            .filter { it.isNotBlank() }
                            .map { line ->
                                val parts = line.split(",").map { it.trim() }
                                StockEntity(
                                    symbol = parts[0],
                                    name = parts[1],
                                    sector = parts[2].takeIf { it != "null" && it.isNotBlank() },
                                    industry = parts[3].takeIf { it != "null" && it.isNotBlank() },
                                    market = market
                                )
                            }
                            .toList()

                        stockDao.insertAll(stocks)
                        Log.d("StockApplication", "Loaded ${stocks.size} stocks for $market")
                    }
                } catch (e: Exception) {
                    Log.e("StockApplication", "Error loading $market data", e)
                    e.printStackTrace()  // 상세 에러 로그
                }
            }
        }
    }
}