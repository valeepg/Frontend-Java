package com.banking.Compartamosapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.banking.Compartamosapp.data.model.Account
import com.banking.Compartamosapp.data.model.Transaction
import com.banking.Compartamosapp.data.model.TransactionType
import com.banking.Compartamosapp.ui.theme.CompartamosappTheme
import com.banking.Compartamosapp.ui.viewmodel.BankViewModel
import com.banking.Compartamosapp.ui.viewmodel.UiState
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: BankViewModel,
    onNavigateToTransaction: () -> Unit
) {
    val accountState by viewModel.accountState.collectAsState()
    val transactionsState by viewModel.transactionsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchData(1L)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Banco Compartamos", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToTransaction,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Hacer Transacción") },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when (val state = accountState) {
                is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                is UiState.Success -> AccountCard(state.data)
                is UiState.Error -> Text("Error: ${state.message}", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Últimas Transacciones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (val state = transactionsState) {
                is UiState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        Text("No hay transacciones recientes")
                    } else {
                        LazyColumn {
                            items(state.data) { transaction ->
                                TransactionItem(transaction)
                            }
                        }
                    }
                }
                is UiState.Error -> Text("Error: ${state.message}", color = Color.Red)
            }
        }
    }
}

@Composable
fun AccountCard(account: Account) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Número de Cuenta: ${account.accountNumber}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Saldo Disponible", style = MaterialTheme.typography.labelLarge)
            Text(
                text = "$${account.balance}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    ListItem(
        headlineContent = { Text(if (transaction.type == TransactionType.DEPOSIT) "Depósito" else "Retiro") },
        supportingContent = { Text(transaction.createdAt ?: "") },
        trailingContent = {
            Text(
                text = "${if (transaction.type == TransactionType.DEPOSIT) "+" else "-"}$${transaction.amount}",
                color = if (transaction.type == TransactionType.DEPOSIT) Color(0xFF4CAF50) else Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    )
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CompartamosappTheme {
        // Mock UI for Preview
        Column(modifier = Modifier.padding(16.dp)) {
            AccountCard(Account(1, "123456789", BigDecimal("1500.50")))
            Spacer(modifier = Modifier.height(24.dp))
            Text("Últimas Transacciones", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            TransactionItem(Transaction(1, TransactionType.DEPOSIT, BigDecimal("500.00"), "2023-10-27T10:00:00", 1))
            TransactionItem(Transaction(2, TransactionType.WITHDRAWAL, BigDecimal("100.00"), "2023-10-27T11:00:00", 1))
        }
    }
}