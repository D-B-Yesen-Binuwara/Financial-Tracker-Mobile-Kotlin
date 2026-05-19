package com.spendly.financetracker.data.model

enum class TransactionType {
    INCOME,
    EXPENSE
}

data class FinanceTransaction(
    val id: String,
    val title: String,
    val amountCents: Long,
    val type: TransactionType,
    val note: String,
    val createdAtMillis: Long
) {
    val signedAmountCents: Long
        get() = if (type == TransactionType.INCOME) amountCents else -amountCents
}

data class TransactionDraft(
    val title: String,
    val amountCents: Long,
    val type: TransactionType,
    val note: String
)
