package com.spendly.financetracker.ui.screen.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.spendly.financetracker.data.model.TransactionType
import com.spendly.financetracker.ui.components.TransactionListItem
import com.spendly.financetracker.ui.viewmodel.FinanceUiState
import com.spendly.financetracker.ui.viewmodel.TransactionTab

typealias OnTransactionTabSelected = (TransactionTab) -> Unit

@Composable
fun TransactionsScreen(
    state: FinanceUiState,
    onTransactionTabSelected: OnTransactionTabSelected
) {
    val filteredTransactions = when (state.transactionTab) {
        TransactionTab.ALL -> state.transactions
        TransactionTab.EXPENSES -> state.transactions.filter { it.type == TransactionType.EXPENSE }
        TransactionTab.INCOMES -> state.transactions.filter { it.type == TransactionType.INCOME }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Transactions",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        }

        item {
            TabRow(selectedTabIndex = state.transactionTab.ordinal) {
                TransactionTab.values().forEachIndexed { index, tab ->
                    Tab(
                        selected = index == state.transactionTab.ordinal,
                        onClick = { onTransactionTabSelected(tab) },
                        text = { Text(tab.title) }
                    )
                }
            }
        }

        if (filteredTransactions.isEmpty()) {
            item {
                Text(
                    text = "No transactions found for ${state.transactionTab.title.lowercase()}.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(filteredTransactions) { transaction ->
                TransactionListItem(transaction = transaction)
            }
        }
    }
}
