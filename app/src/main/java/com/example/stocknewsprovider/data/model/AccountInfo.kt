package com.example.stocknewsprovider.data.model
// 2. 데이터 모델 클래스 생성

data class AccountInfo(
    val accountNumber: String,
    val stockHoldings: List<StockHolding>
)

data class StockHolding(
    val stockName: String,
    val quantity: Int,
    val currentPrice: Double
)