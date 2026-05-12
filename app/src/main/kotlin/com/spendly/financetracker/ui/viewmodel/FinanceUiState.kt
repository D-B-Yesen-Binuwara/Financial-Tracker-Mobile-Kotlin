package com.spendly.financetracker.ui.viewmodel

import com.spendly.financetracker.data.model.FinanceTransaction
import com.spendly.financetracker.data.model.TransactionType
import com.spendly.financetracker.data.model.UserSession

enum class AuthMode {
    SIGN_IN,
    CREATE_ACCOUNT
}

data class FinanceUiState(
    val isFirebaseConfigured: Boolean = true,
    val isLoading: Boolean = true,
    val isBusy: Boolean = false,
    val session: UserSession? = null,
    val authMode: AuthMode = AuthMode.SIGN_IN,
    val email: String = "",
    val password: String = "",
    val transactionTitle: String = "",
    val transactionAmount: String = "",
    val transactionNote: String = "",
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val transactions: List<FinanceTransaction> = emptyList(),
    val message: String? = null
) {
    val incomeCents: Long
        get() = transactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amountCents }

    val expenseCents: Long
        get() = transactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amountCents }

    val balanceCents: Long
        get() = transactions.sumOf { it.signedAmountCents }
}
