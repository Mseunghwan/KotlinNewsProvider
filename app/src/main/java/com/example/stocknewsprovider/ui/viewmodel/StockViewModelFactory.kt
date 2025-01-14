// ui/viewmodel/StockViewModelFactory.kt
package com.example.stocknewsprovider.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stocknewsprovider.data.api.KiwoomApiService
import com.example.stocknewsprovider.data.repository.StockRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StockViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
            // API Service 생성
            val apiService = createKiwoomApiService()
            // Repository 생성
            val repository = StockRepository(apiService)
            // ViewModel 생성
            @Suppress("UNCHECKED_CAST")
            return StockViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private fun createKiwoomApiService(): KiwoomApiService {
        // Retrofit 설정 및 API Service 생성
        return Retrofit.Builder()
            .baseUrl("https://your-api-base-url/")  // 실제 키움증권 API URL로 변경 필요
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KiwoomApiService::class.java)
    }
}