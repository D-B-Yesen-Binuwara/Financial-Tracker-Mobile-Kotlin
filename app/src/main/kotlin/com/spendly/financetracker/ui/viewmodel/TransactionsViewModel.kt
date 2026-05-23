package com.spendly.financetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spendly.financetracker.data.model.FinanceTransaction
import com.spendly.financetracker.data.model.TransactionType
import com.spendly.financetracker.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TransactionsUiState(
    val transactions: List<FinanceTransaction> = emptyList(),
    val filter: TransactionTab = TransactionTab.ALL,
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val filtered: List<FinanceTransaction>
        get() = when (filter) {
            TransactionTab.ALL -> transactions
            TransactionTab.EXPENSES -> transactions.filter { it.type == TransactionType.EXPENSE }
            TransactionTab.INCOMES -> transactions.filter { it.type == TransactionType.INCOME }
        }.sortedByDescending { it.createdAtMillis }
}

class TransactionsViewModel(
    private val transactionRepository: TransactionRepository,
    private val userId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = _uiState.asStateFlow()

    init {
        observeTransactions()
    }

    fun setFilter(tab: TransactionTab) {
        _uiState.update { it.copy(filter = tab) }
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            transactionRepository.observeTransactions(userId)
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { list -> _uiState.update { it.copy(transactions = list, isLoading = false) } }
        }
    }

    class Factory(
        private val transactionRepository: TransactionRepository,
        private val userId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            TransactionsViewModel(transactionRepository, userId) as T
    }
}
