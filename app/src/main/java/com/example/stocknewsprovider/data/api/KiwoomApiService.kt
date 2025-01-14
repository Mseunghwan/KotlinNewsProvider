package com.example.stocknewsprovider.data.api

import com.example.stocknewsprovider.data.model.AccountInfo
import retrofit2.http.Body
import retrofit2.http.POST

// 3. API 인터페이스 정의

interface KiwoomApiService {
    @POST("account/info")
    suspend fun getAccountInfo(
        @Body credentials: Map<String, String>
    ): AccountInfo
}