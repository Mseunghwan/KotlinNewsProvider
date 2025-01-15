package com.example.stocknewsprovider.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stocknewsprovider.data.entity.StockEntity
import com.example.stocknewsprovider.ui.dialog.StockSearchDialog
import com.example.stocknewsprovider.ui.viewmodel.StockViewModel

// 8. 브로커 선택 화면
// ui/screens/BrokerSelectionScreen.kt
@Composable
fun BrokerSelectionScreen(
    navController: NavController,
    viewModel: StockViewModel
) {
    var showSearchDialog by remember { mutableStateOf(false) }
    val selectedStocks by viewModel.selectedStocks.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "보유하고 계신 종목을 선택해주세요",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        Button(
            onClick = { showSearchDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("포트폴리오 추가하기")
        }

        // 선택된 종목들 표시
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(selectedStocks) { stock ->
                SelectedStockItem(
                    stock = stock,
                    onRemove = { viewModel.removeStockFromPortfolio(stock) }
                )
            }
        }

        if (showSearchDialog) {
            StockSearchDialog(
                onDismissRequest = { showSearchDialog = false },
                onStockSelected = { stock ->
                    viewModel.addStockToPortfolio(stock)
                    Log.d("BrokerSelectionScreen", "Stock selected: ${stock.name}")
                },
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun SelectedStockItem(
    stock: StockEntity,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stock.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${stock.market} | ${stock.symbol}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove"
                )
            }
        }
    }
}