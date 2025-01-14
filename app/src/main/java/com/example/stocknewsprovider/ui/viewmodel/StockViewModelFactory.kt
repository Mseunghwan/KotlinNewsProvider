// ui/viewmodel/StockViewModelFactory.kt
package com.example.stocknewsprovider.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stocknewsprovider.data.api.KiwoomApiService
import com.example.stocknewsprovider.data.dao.StockDao
import com.example.stocknewsprovider.data.repository.StockRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// ui/viewmodel/StockViewModelFactory.kt
class StockViewModelFactory(private val stockDao: StockDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StockViewModel(stockDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}