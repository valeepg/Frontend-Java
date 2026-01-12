package com.banking.Compartamosapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.banking.Compartamosapp.data.model.Transaction
import com.banking.Compartamosapp.data.model.TransactionType
import com.banking.Compartamosapp.ui.theme.CompartamosappTheme
import com.banking.Compartamosapp.ui.viewmodel.BankViewModel
import com.banking.Compartamosapp.ui.viewmodel.UiState
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf(TransactionType.DEPOSIT) }
    val accountState by viewModel.accountState.collectAsState()
    val transactionResult by viewModel.transactionResult.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(transactionResult) {
        when (transactionResult) {
            is UiState.Success -> {
                Toast.makeText(context, "Transacción Exitosa", Toast.LENGTH_SHORT).show()
                viewModel.resetTransactionResult()
                onNavigateBack()
            }
            is UiState.Error -> {
                Toast.makeText(context, (transactionResult as UiState.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetTransactionResult()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Transacción", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Monto") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Tipo de Transacción", style = MaterialTheme.typography.titleMedium)
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = transactionType == TransactionType.DEPOSIT,
                    onClick = { transactionType = TransactionType.DEPOSIT }
                )
                Text("Depósito")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = transactionType == TransactionType.WITHDRAWAL,
                    onClick = { transactionType = TransactionType.WITHDRAWAL }
                )
                Text("Retiro")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val inputAmount = amount.toBigDecimalOrNull()
                    if (inputAmount != null && inputAmount > BigDecimal.ZERO) {
                        
                        val currentBalance = (accountState as? UiState.Success)?.data?.balance ?: BigDecimal.ZERO
                        
                        if (transactionType == TransactionType.WITHDRAWAL && inputAmount > currentBalance) {
                            Toast.makeText(context, "Saldo insuficiente para realizar el retiro", Toast.LENGTH_LONG).show()
                        } else {
                            viewModel.createTransaction(
                                Transaction(
                                    type = transactionType,
                                    amount = inputAmount,
                                    accountId = 1L // Default account ID
                                )
                            )
                        }
                    } else {
                        Toast.makeText(context, "Ingrese un monto mayor a cero", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                enabled = transactionResult !is UiState.Loading
            ) {
                if (transactionResult is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Confirmar Transacción", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview() {
    CompartamosappTheme {
        var amount by remember { mutableStateOf("100.00") }
        var transactionType by remember { mutableStateOf(TransactionType.DEPOSIT) }

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Monto") }, modifier = Modifier.fillMaxWidth())
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = transactionType == TransactionType.DEPOSIT, onClick = {})
                Text("Depósito")
                RadioButton(selected = transactionType == TransactionType.WITHDRAWAL, onClick = {})
                Text("Retiro")
            }
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text("Confirmar Transacción")
            }
        }
    }
}