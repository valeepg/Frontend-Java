package com.banking.Compartamosapp.data.model

import java.math.BigDecimal

data class Account(
    val id: Long,
    val accountNumber: String,
    val balance: BigDecimal
)