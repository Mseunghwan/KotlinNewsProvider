// ui/viewmodel/StockViewModel.kt
package com.example.stocknewsprovider.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stocknewsprovider.data.dao.StockDao
import com.example.stocknewsprovider.data.entity.StockEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// ui/viewmodel/StockViewModel.kt

class StockViewModel(private val stockDao: StockDao) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<StockEntity>>(emptyList())
    val searchResults: StateFlow<List<StockEntity>> = _searchResults

    private val _selectedStocks = MutableStateFlow<List<StockEntity>>(emptyList())
    val selectedStocks: StateFlow<List<StockEntity>> = _selectedStocks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun searchStocks(market: String?, query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = if (market != null) {
                    stockDao.searchStocksByMarket(market, query)
                } else {
                    stockDao.searchAllStocks(query)
                }.first()  // Flow를 일회성 List로 변환
                _searchResults.value = results
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
}