package com.banking.Compartamosapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.banking.Compartamosapp.data.model.Account
import com.banking.Compartamosapp.data.model.Transaction
import com.banking.Compartamosapp.data.repository.BankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class BankViewModel(private val repository: BankRepository) : ViewModel() {

    private val _accountState = MutableStateFlow<UiState<Account>>(UiState.Loading)
    val accountState: StateFlow<UiState<Account>> = _accountState.asStateFlow()

    private val _transactionsState = MutableStateFlow<UiState<List<Transaction>>>(UiState.Loading)
    val transactionsState: StateFlow<UiState<List<Transaction>>> = _transactionsState.asStateFlow()

    private val _transactionResult = MutableStateFlow<UiState<Transaction>?>(null)
    val transactionResult: StateFlow<UiState<Transaction>?> = _transactionResult.asStateFlow()

    fun fetchData(accountId: Long = 1L) {
        fetchAccount(accountId)
        fetchTransactions(accountId)
    }

    private fun fetchAccount(id: Long) {
        viewModelScope.launch {
            _accountState.value = UiState.Loading
            try {
                val account = repository.getAccount(id)
                _accountState.value = UiState.Success(account)
            } catch (e: Exception) {
                _accountState.value = UiState.Error(e.message ?: "Error al cargar cuenta")
            }
        }
    }

    private fun fetchTransactions(accountId: Long) {
        viewModelScope.launch {
            _transactionsState.value = UiState.Loading
            try {
                val transactions = repository.getTransactions(accountId)
                _transactionsState.value = UiState.Success(transactions)
            } catch (e: Exception) {
                _transactionsState.value = UiState.Error(e.message ?: "Error al cargar transacciones")
            }
        }
    }

    fun createTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _transactionResult.value = UiState.Loading
            try {
                val result = repository.createTransaction(transaction)
                _transactionResult.value = UiState.Success(result)
                // Refresh data after successful transaction
                fetchData(transaction.accountId)
            } catch (e: Exception) {
                _transactionResult.value = UiState.Error(e.message ?: "Error al crear transacción")
            }
        }
    }

    fun resetTransactionResult() {
        _transactionResult.value = null
    }
}