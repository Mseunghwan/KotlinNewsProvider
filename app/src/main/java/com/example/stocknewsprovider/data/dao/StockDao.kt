package com.example.stocknewsprovider.data.dao

import androidx.room.*
import com.example.stocknewsprovider.data.entity.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {

    @Query("SELECT * FROM stocks WHERE market = :market AND (symbol LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%')")
    suspend fun searchStocksByMarket(market: String, query: String): List<StockEntity>

    @Query("SELECT * FROM stocks WHERE symbol LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%'")
    suspend fun searchAllStocks(query: String): List<StockEntity>

    @Query("SELECT COUNT(*) FROM stocks")
    suspend fun getStockCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stocks: List<StockEntity>)

    @Query("DELETE FROM stocks WHERE market = :market")
    suspend fun deleteByMarket(market: String)

    @Query("SELECT DISTINCT market FROM stocks ORDER BY market ASC")
    suspend fun getAvailableMarkets(): List<String>

    @Query("SELECT * FROM stocks LIMIT 5")
    suspend fun getFirstFiveStocks(): List<StockEntity>
}