package com.banking.Compartamosapp.data.repository

import com.banking.Compartamosapp.data.model.Account
import com.banking.Compartamosapp.data.model.Transaction
import com.banking.Compartamosapp.data.remote.ApiService

interface BankRepository {
    suspend fun getAccount(id: Long): Account
    suspend fun getTransactions(accountId: Long): List<Transaction>
    suspend fun createTransaction(transaction: Transaction): Transaction
}

class BankRepositoryImpl(private val apiService: ApiService) : BankRepository {
    override suspend fun getAccount(id: Long): Account = apiService.getAccount(id)
    override suspend fun getTransactions(accountId: Long): List<Transaction> = apiService.getTransactions(accountId)
    override suspend fun createTransaction(transaction: Transaction): Transaction = apiService.createTransaction(transaction)
}