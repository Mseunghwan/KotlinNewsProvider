// 5. ui/dialog/StockSearchDialog.kt
package com.example.stocknewsprovider.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stocknewsprovider.data.entity.StockEntity
import com.example.stocknewsprovider.ui.viewmodel.StockViewModel

@Composable
fun StockSearchDialog(
    onDismissRequest: () -> Unit,
    onStockSelected: (StockEntity) -> Unit,
    viewModel: StockViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedMarket by remember { mutableStateOf("KOSPI") }
    val stocks by viewModel.searchResults.collectAsState()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("주식 검색") },
        text = {
            Column {
                // 마켓 선택 탭
                TabRow(
                    selectedTabIndex = when(selectedMarket) {
                        "NYSE" -> 0
                        "NASDAQ" -> 1
                        else -> 2
                    }
                ) {
                    Tab(
                        selected = selectedMarket == "NYSE",
                        onClick = { selectedMarket = "NYSE" }
                    ) { Text("NYSE") }
                    Tab(
                        selected = selectedMarket == "NASDAQ",
                        onClick = { selectedMarket = "NASDAQ" }
                    ) { Text("NASDAQ") }
                    Tab(
                        selected = selectedMarket == "KOSPI",
                        onClick = { selectedMarket = "KOSPI" }
                    ) { Text("KOSPI") }
                }

                // 검색창
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.searchStocks(selectedMarket, it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text("검색어를 입력하세요") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                )

                // 검색 결과 리스트
                LazyColumn(
                    modifier = Modifier.height(400.dp)
                ) {
                    items(stocks) { stock ->
                        StockItem(
                            stock = stock,
                            onClick = {
                                onStockSelected(stock)
                                onDismissRequest()
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("닫기")
            }
        }
    )
}

@Composable
private fun StockItem(
    stock: StockEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stock.symbol,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stock.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}