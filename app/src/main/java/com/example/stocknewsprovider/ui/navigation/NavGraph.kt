// ui/navigation/NavGraph.kt
package com.example.stocknewsprovider.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stocknewsprovider.ui.screens.BrokerSelectionScreen
import com.example.stocknewsprovider.ui.screens.AccountInfoScreen
import com.example.stocknewsprovider.ui.viewmodel.StockViewModel
import com.example.stocknewsprovider.ui.viewmodel.StockViewModelFactory

// NavGraph.kt 수정
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val factory = StockViewModelFactory()
    val viewModel: StockViewModel = viewModel(factory = factory)

    NavHost(navController = navController, startDestination = "broker_selection") {
        composable("broker_selection") {
            BrokerSelectionScreen(navController)
        }
        composable("account_info") {
            AccountInfoScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}