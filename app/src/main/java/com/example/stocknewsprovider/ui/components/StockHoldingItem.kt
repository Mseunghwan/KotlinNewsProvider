package com.example.stocknewsprovider.ui.components

// ui/components/StockHoldingItem.kt

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stocknewsprovider.data.model.StockHolding

@Composable
fun StockHoldingItem(holding: StockHolding) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = holding.stockName,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "수량: ${holding.quantity}주",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "현재가: ${String.format("%,d", holding.currentPrice.toInt())}원",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}