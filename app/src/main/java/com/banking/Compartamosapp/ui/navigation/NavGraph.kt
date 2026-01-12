package com.banking.Compartamosapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.banking.Compartamosapp.data.remote.ApiService
import com.banking.Compartamosapp.data.repository.BankRepositoryImpl
import com.banking.Compartamosapp.ui.screens.HomeScreen
import com.banking.Compartamosapp.ui.screens.TransactionScreen
import com.banking.Compartamosapp.ui.viewmodel.BankViewModel
import com.banking.Compartamosapp.ui.viewmodel.BankViewModelFactory

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Transaction : Screen("transaction")
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    
    // In a real app, use Hilt or Koin for DI
    val apiService = ApiService.create()
    val repository = BankRepositoryImpl(apiService)
    val viewModel: BankViewModel = viewModel(
        factory = BankViewModelFactory(repository)
    )

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToTransaction = { navController.navigate(Screen.Transaction.route) }
            )
        }
        composable(Screen.Transaction.route) {
            TransactionScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}