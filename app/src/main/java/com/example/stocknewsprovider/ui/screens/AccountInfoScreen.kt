package com.example.stocknewsprovider.ui.screens
// 9. 계좌 정보 화면
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stocknewsprovider.ui.components.StockHoldingItem
import com.example.stocknewsprovider.ui.viewmodel.StockViewModel

// ui/screens/AccountInfoScreen.kt
@Composable
fun AccountInfoScreen(
    navController: NavController,
    viewModel: StockViewModel
) {
    val accountInfo by viewModel.accountInfo.collectAsState(initial = null)
    val isLoading by viewModel.isLoading.collectAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 뒤로가기 버튼 추가
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            accountInfo?.let { info ->
                LazyColumn {
                    items(info.stockHoldings) { holding ->
                        StockHoldingItem(holding)
                    }
                }
            }
        }
    }
}