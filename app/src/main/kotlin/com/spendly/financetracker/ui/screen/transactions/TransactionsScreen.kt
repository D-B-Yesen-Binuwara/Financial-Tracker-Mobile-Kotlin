package com.spendly.financetracker.ui.screen.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendly.financetracker.data.model.FinanceTransaction
import com.spendly.financetracker.data.model.TransactionType
import com.spendly.financetracker.ui.components.NoTransactionsState
import com.spendly.financetracker.ui.components.TransactionListItem
import com.spendly.financetracker.ui.theme.SpendlyGray100
import com.spendly.financetracker.ui.theme.SpendlyGray300
import com.spendly.financetracker.ui.theme.SpendlyGray500
import com.spendly.financetracker.ui.theme.SpendlyGray700
import com.spendly.financetracker.ui.theme.SpendlyGreen
import com.spendly.financetracker.ui.util.currentMonthLabel
import com.spendly.financetracker.ui.util.formatDateFull
import com.spendly.financetracker.ui.viewmodel.FinanceUiState
import com.spendly.financetracker.ui.viewmodel.TransactionTab

typealias OnTransactionTabSelected = (TransactionTab) -> Unit

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TransactionsScreen(
    state: FinanceUiState,
    onTransactionTabSelected: OnTransactionTabSelected,
    onAddExpense: () -> Unit,
    onAddIncome: () -> Unit,
    onDeleteTransaction: (String) -> Unit
) {
    val filteredTransactions = when (state.transactionTab) {
        TransactionTab.ALL -> state.transactions
        TransactionTab.EXPENSES -> state.transactions.filter { it.type == TransactionType.EXPENSE }
        TransactionTab.INCOMES -> state.transactions.filter { it.type == TransactionType.INCOME }
    }.sortedByDescending { it.createdAtMillis }

    val groupedTransactions = filteredTransactions.groupBy { transaction ->
        formatDateFull(transaction.createdAtMillis)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Transactions",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Surface(
                        color = SpendlyGray100,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CalendarMonth,
                                null,
                                tint = SpendlyGray700,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.size(4.dp))
                            Text(
                                currentMonthLabel(),
                                style = MaterialTheme.typography.labelMedium,
                                color = SpendlyGray700
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TransactionTab.values().forEach { tab ->
                        FilterChip(
                            selected = state.transactionTab == tab,
                            onClick = { onTransactionTabSelected(tab) },
                            label = { Text(tab.title) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SpendlyGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${filteredTransactions.size} transactions",
                        style = MaterialTheme.typography.labelSmall,
                        color = SpendlyGray500,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedButton(
                        onClick = onAddIncome,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Text("Income", style = MaterialTheme.typography.labelMedium)
                    }
                    OutlinedButton(
                        onClick = onAddExpense,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Text("Expense", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            HorizontalDivider(thickness = 0.5.dp, color = SpendlyGray300)

            if (filteredTransactions.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    NoTransactionsState(onAddExpense = onAddExpense)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    groupedTransactions.forEach { (dateLabel, transactions) ->
                        stickyHeader {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SpendlyGray100)
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "$dateLabel - ${transactions.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SpendlyGray500,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        items(
                            items = transactions,
                            key = FinanceTransaction::id
                        ) { transaction ->
                            TransactionListItem(
                                transaction = transaction,
                                showContainer = false,
                                onDelete = { onDeleteTransaction(transaction.id) }
                            )
                            HorizontalDivider(thickness = 0.5.dp, color = SpendlyGray100)
                        }
                    }
                }
            }
        }
    }
}
