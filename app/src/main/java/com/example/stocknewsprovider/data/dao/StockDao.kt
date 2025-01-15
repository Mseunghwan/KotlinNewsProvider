package com.example.stocknewsprovider.data.dao

import androidx.room.*
import com.example.stocknewsprovider.data.entity.StockEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface StockDao {
    @Query("SELECT * FROM stocks WHERE market = :market AND (symbol LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%') LIMIT 10")
    fun searchStocksByMarket(market: String, query: String): Flow<List<StockEntity>>

    @Query("SELECT * FROM stocks WHERE symbol LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%' LIMIT 10")
    fun searchAllStocks(query: String): Flow<List<StockEntity>>

    @Query("SELECT * FROM stocks WHERE market = :market LIMIT 100")
    suspend fun getStocksByMarket(market: String): List<StockEntity>

    @Query("SELECT COUNT(*) FROM stocks")
    suspend fun getStockCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stocks: List<StockEntity>)
}
