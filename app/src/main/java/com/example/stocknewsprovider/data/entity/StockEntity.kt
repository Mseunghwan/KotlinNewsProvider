package com.example.stocknewsprovider.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey val symbol: String,
    val name: String,
    val sector: String?,
    val industry: String?,
    val market: String  // KOSPI, NASDAQ, NYSE
)