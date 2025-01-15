// 5. ui/dialog/StockSearchDialog.kt
package com.example.stocknewsprovider.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
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
    var selectedMarket by remember { mutableStateOf("ALL") }
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("주식 검색") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // 마켓 선택 칩
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 8.dp)
                ) {
                    FilterChip(
                        selected = selectedMarket == "ALL",
                        onClick = {
                            selectedMarket = "ALL"
                            viewModel.searchStocks(null, searchQuery)
                        },
                        label = { Text("전체") }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    listOf("KOSPI", "NASDAQ", "NYSE").forEach { market ->
                        FilterChip(
                            selected = selectedMarket == market,
                            onClick = {
                                selectedMarket = market
                                viewModel.searchStocks(market, searchQuery)
                            },
                            label = { Text(market) }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }

                // 검색창
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { newQuery ->
                        searchQuery = newQuery
                        viewModel.searchStocks(selectedMarket, newQuery)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("종목명 또는 심볼 입력") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 검색 결과 또는 로딩 표시
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.height(400.dp)
                    ) {
                        items(searchResults) { stock ->
                            StockSearchResultItem(
                                stock = stock,
                                onClick = {
                                    onStockSelected(stock)
                                    onDismissRequest()
                                }
                            )
                        }
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
private fun StockSearchResultItem(
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
                text = stock.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${stock.market} | ${stock.symbol}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            stock.sector?.let { sector ->
                Text(
                    text = sector,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}