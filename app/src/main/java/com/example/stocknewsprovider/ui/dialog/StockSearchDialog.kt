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
    var selectedMarket by remember { mutableStateOf<String?>(null) }
    val stocks by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxHeight(0.9f),
        title = { Text("주식 검색") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Market Selection Chips
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 8.dp)
                ) {
                    FilterChip(
                        selected = selectedMarket == null,
                        onClick = {
                            selectedMarket = null
                            viewModel.searchStocks(null, searchQuery)
                        },
                        label = { Text("전체") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    listOf("KOSPI", "NASDAQ", "NYSE").forEach { market ->
                        FilterChip(
                            selected = selectedMarket == market,
                            onClick = {
                                selectedMarket = market
                                viewModel.searchStocks(market, searchQuery)
                            },
                            label = { Text(market) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                // Search TextField with auto-complete
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query
                        viewModel.searchStocks(selectedMarket, query)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text("종목명 또는 심볼을 입력하세요") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                )

                // Search Results
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn {
                        items(stocks) { stock ->
                            StockSearchItem(
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
private fun StockSearchItem(
    stock: StockEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stock.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stock.symbol,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            stock.sector?.let { sector ->
                Text(
                    text = "${stock.market} | $sector",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}