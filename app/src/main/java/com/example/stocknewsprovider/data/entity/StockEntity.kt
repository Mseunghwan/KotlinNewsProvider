package com.example.stocknewsprovider.data.entity

// 2. data/entity/StockEntity.kt

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey val symbol: String,
    val name: String,
    val sector: String? = null,
    val industry: String? = null
)