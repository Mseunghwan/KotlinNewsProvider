package com.example.stocknewsprovider.data.model

data class ScreenerResponse(
    val finance: Finance
)

data class Finance(
    val result: List<Result>
)

data class Result(
    val quotes: List<Quote>
)

data class Quote(
    val symbol: String,
    val shortName: String,
    val longName: String?,
    val sector: String?,
    val industry: String?
)