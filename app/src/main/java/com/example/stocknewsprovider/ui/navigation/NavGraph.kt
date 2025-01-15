// ui/navigation/NavGraph.kt
package com.example.stocknewsprovider.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stocknewsprovider.data.database.AppDatabase
import com.example.stocknewsprovider.ui.screens.BrokerSelectionScreen
import com.example.stocknewsprovider.ui.screens.AccountInfoScreen
import com.example.stocknewsprovider.ui.viewmodel.StockViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val viewModel: StockViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
    )

    NavHost(navController = navController, startDestination = "broker_selection") {
        composable("broker_selection") {
            BrokerSelectionScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable("account_info") {
            AccountInfoScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}