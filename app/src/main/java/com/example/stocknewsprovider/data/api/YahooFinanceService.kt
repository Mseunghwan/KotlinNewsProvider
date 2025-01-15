package com.example.stocknewsprovider.data.api

import com.example.stocknewsprovider.data.model.ScreenerResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface YahooFinanceService {
    @GET("v8/finance/screener/predefined/saved")
    suspend fun getStocks(
        @Query("formatted") formatted: Boolean = true,
        @Query("lang") lang: String = "en-US",
        @Query("region") region: String = "US",
        @Query("scrIds") scrIds: String = "day_gainers",
        @Query("count") count: Int = 250
    ): ScreenerResponse
}