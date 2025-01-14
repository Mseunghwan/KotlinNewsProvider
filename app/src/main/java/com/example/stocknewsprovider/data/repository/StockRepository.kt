package com.example.stocknewsprovider.data.repository

import com.example.stocknewsprovider.data.model.AccountInfo
import com.example.stocknewsprovider.data.api.KiwoomApiService

// 4. Repository 생성
class StockRepository(private val apiService: KiwoomApiService){
    suspend fun getAccountInfo(credentials: Map<String, String>): AccountInfo {
        return apiService.getAccountInfo(credentials)
    }
}