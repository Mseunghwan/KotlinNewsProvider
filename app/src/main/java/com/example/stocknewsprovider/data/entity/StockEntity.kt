package com.example.stocknewsprovider.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey val symbol: String,
    val name: String,
    val market: String,  // KOSPI, NASDAQ, NYSE
    val sector: String? = null,
    val industry: String? = null  // industry 필드 추가
)
