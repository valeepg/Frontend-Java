package com.banking.Compartamosapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.banking.Compartamosapp.data.repository.BankRepository

class BankViewModelFactory(private val repository: BankRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BankViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BankViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}