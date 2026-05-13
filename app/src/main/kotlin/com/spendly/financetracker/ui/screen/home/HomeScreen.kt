package com.spendly.financetracker.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.spendly.financetracker.ui.components.HeaderSection
import com.spendly.financetracker.ui.components.OnSignOut
import com.spendly.financetracker.ui.components.PrimaryGoalCard
import com.spendly.financetracker.ui.components.SummaryPanel
import com.spendly.financetracker.ui.components.TransactionListItem
import com.spendly.financetracker.ui.viewmodel.FinanceUiState

@Composable
fun HomeScreen(state: FinanceUiState, onSignOut: OnSignOut) {
    val recentTransactions = state.transactions
        .sortedByDescending { it.createdAtMillis }
        .take(4)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            HeaderSection(
                title = "Good morning",
                subtitle = state.session?.email.orEmpty(),
                onSignOut = onSignOut
            )
        }

        item {
            SummaryPanel(
                incomeCents = state.incomeCents,
                expenseCents = state.expenseCents,
                savingsCents = state.balanceCents,
                savingsRate = state.savingsRate
            )
        }

        item {
            state.primaryGoal?.let { goal ->
                PrimaryGoalCard(goal = goal)
            }
        }

        item {
            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (recentTransactions.isEmpty()) {
            item {
                Text(
                    text = "No recent transactions yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(recentTransactions) { transaction ->
                TransactionListItem(transaction = transaction)
            }
        }
    }
}
