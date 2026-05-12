package com.spendly.financetracker.ui.viewmodel

import com.spendly.financetracker.data.model.FinanceTransaction
import com.spendly.financetracker.data.model.TransactionType
import com.spendly.financetracker.data.model.UserSession

enum class AuthMode {
    SIGN_IN,
    CREATE_ACCOUNT
}

enum class AppTab {
    HOME,
    TRANSACTIONS,
    GOALS,
    ANALYTICS,
    PROFILE
}

enum class TransactionTab(val title: String) {
    ALL("All"),
    EXPENSES("Expenses"),
    INCOMES("Incomes")
}

data class Goal(
    val id: String,
    val title: String,
    val targetCents: Long,
    val savedCents: Long,
    val dueDate: String,
    val category: String,
    val isPrimary: Boolean = false
) {
    val progressPercent: Int
        get() = if (targetCents <= 0L) 0 else ((savedCents * 100) / targetCents).toInt().coerceIn(0, 100)

    val remainingCents: Long
        get() = (targetCents - savedCents).coerceAtLeast(0L)
}

private fun defaultGoals() = listOf(
    Goal(
        id = "primary-goal",
        title = "MacBook Pro M4",
        targetCents = 490_000_00L,
        savedCents = 107_200_00L,
        dueDate = "May 2027",
        category = "Gadgets",
        isPrimary = true
    ),
    Goal(
        id = "emergency-fund",
        title = "Emergency Fund",
        targetCents = 200_000_00L,
        savedCents = 150_000_00L,
        dueDate = "Ongoing",
        category = "Safety"
    ),
    Goal(
        id = "trip-ella",
        title = "Trip to Ella",
        targetCents = 50_000_00L,
        savedCents = 10_000_00L,
        dueDate = "Aug 2026",
        category = "Travel"
    )
)

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
    val currentTab: AppTab = AppTab.HOME,
    val transactionTab: TransactionTab = TransactionTab.ALL,
    val goals: List<Goal> = defaultGoals(),
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

    val primaryGoal: Goal?
        get() = goals.firstOrNull { it.isPrimary } ?: goals.firstOrNull()

    val savingsRate: Int
        get() = if (incomeCents <= 0L) 0 else (((incomeCents - expenseCents) * 100) / incomeCents).toInt().coerceIn(0, 100)
}
