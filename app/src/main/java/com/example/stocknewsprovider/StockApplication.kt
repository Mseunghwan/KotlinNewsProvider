package com.example.stocknewsprovider

import android.app.Application
import android.util.Log
import com.example.stocknewsprovider.data.database.AppDatabase
import com.example.stocknewsprovider.data.entity.StockEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// assets/stocks.csv 파일 예시:
/*
symbol,name,market,sector,currency
005930,삼성전자,KOSPI,전자,KRW
373220,LG에너지솔루션,KOSPI,전자,KRW
...
*/

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
            try {
                assets.open("stocks.csv").bufferedReader().use { reader ->
                    // 헤더 건너뛰기
                    reader.readLine()

                    // 데이터 읽기
                    val stocks = reader.lineSequence()
                        .filter { it.isNotBlank() }
                        .map { line ->
                            val (symbol, name, sector, industry) = line.split(",")
                            StockEntity(symbol, name, sector, industry)
                        }
                        .toList()

                    stockDao.insertAll(stocks)
                }
            } catch (e: Exception) {
                Log.e("StockApplication", "Error loading initial data", e)
            }
        }
    }
}