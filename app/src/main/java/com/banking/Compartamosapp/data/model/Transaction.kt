package com.banking.Compartamosapp.data.model

import java.math.BigDecimal

data class Transaction(
    val id: Long? = null,
    val type: TransactionType,
    val amount: BigDecimal,
    val createdAt: String? = null,
    val accountId: Long
)