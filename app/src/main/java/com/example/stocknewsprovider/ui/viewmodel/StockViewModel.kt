package com.example.stocknewsprovider.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocknewsprovider.data.database.AppDatabase
import com.example.stocknewsprovider.data.entity.StockEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DbStatus {
    object Loading : DbStatus()
    object Ready : DbStatus()
    data class Error(val message: String) : DbStatus()
}

class StockViewModel(application: Application) : AndroidViewModel(application) {
    private val _dbStatus = MutableStateFlow<DbStatus>(DbStatus.Loading)
    val dbStatus: StateFlow<DbStatus> = _dbStatus

    init {
        checkDatabaseStatus()
    }

    private fun checkDatabaseStatus() {
        viewModelScope.launch {
            try {
                val count = stockDao.getStockCount()
                if (count > 0) {
                    _dbStatus.value = DbStatus.Ready
                } else {
                    _dbStatus.value = DbStatus.Error("데이터베이스가 비어있습니다.")
                }
                android.util.Log.d("StockViewModel", "Database count: $count")
            } catch (e: Exception) {
                _dbStatus.value = DbStatus.Error(e.message ?: "알 수 없는 오류")
                android.util.Log.e("StockViewModel", "Database error", e)
            }
        }
    }

    private val stockDao = AppDatabase.getDatabase(application).stockDao()

    private val _searchResults = MutableStateFlow<List<StockEntity>>(emptyList())
    val searchResults: StateFlow<List<StockEntity>> = _searchResults

    private val _selectedStocks = MutableStateFlow<List<StockEntity>>(emptyList())
    val selectedStocks: StateFlow<List<StockEntity>> = _selectedStocks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _availableMarkets = MutableStateFlow<List<String>>(emptyList())
    val availableMarkets: StateFlow<List<String>> = _availableMarkets

    init {
        loadAvailableMarkets()
    }

    private fun loadAvailableMarkets() {
        viewModelScope.launch {
            try {
                _availableMarkets.value = stockDao.getAvailableMarkets()
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    fun searchStocks(market: String?, query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = if (market != null && market != "ALL") {
                    stockDao.searchStocksByMarket(market, query)
                } else {
                    stockDao.searchAllStocks(query)
                }
                _searchResults.value = results
            } catch (e: Exception) {
                Log.e("StockViewModel", "Search error", e)
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addStockToPortfolio(stock: StockEntity) {
        val currentList = _selectedStocks.value.toMutableList()
        if (!currentList.contains(stock)) {
            currentList.add(stock)
            _selectedStocks.value = currentList
        }
    }

    fun removeStockFromPortfolio(stock: StockEntity) {
        val currentList = _selectedStocks.value.toMutableList()
        currentList.remove(stock)
        _selectedStocks.value = currentList
    }
    fun retryDatabaseLoad() {
        viewModelScope.launch {
            _dbStatus.value = DbStatus.Loading
            delay(500) // 재시도 전 잠시 대기
            checkDatabaseStatus()
        }
    }

    fun debugDatabase() {
        viewModelScope.launch {
            val count = stockDao.getStockCount()
            android.util.Log.d("StockViewModel", "Total stocks in DB: $count")

            val markets = stockDao.getAvailableMarkets()
            android.util.Log.d("StockViewModel", "Available markets: $markets")

            val sampleStocks = stockDao.getFirstFiveStocks()
            android.util.Log.d("StockViewModel", "Sample stocks: $sampleStocks")
        }
    }
}