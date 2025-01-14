package com.example.stocknewsprovider.data.dao

import androidx.room.*
import com.example.stocknewsprovider.data.entity.StockEntity

@Dao
interface StockDao {
    @Query("SELECT * FROM stocks WHERE market = :market AND (symbol LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%')")
    suspend fun searchStocks(market: String, query: String): List<StockEntity>

    @Query("SELECT * FROM stocks WHERE market = :market LIMIT 100")
    suspend fun getStocksByMarket(market: String): List<StockEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stocks: List<StockEntity>)

    @Query("SELECT COUNT(*) FROM stocks")
    suspend fun getStockCount(): Int
}