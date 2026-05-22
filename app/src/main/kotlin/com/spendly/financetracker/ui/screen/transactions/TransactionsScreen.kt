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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onAddIncome: () -> Unit
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                                    showContainer = false
                                )
                                HorizontalDivider(thickness = 0.5.dp, color = SpendlyGray100)
                            }
                        }
                    }
                }
            }

            // Floating action button + expandable actions (bottom-right above nav bar)
            var fabExpanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.End
            ) {
                if (fabExpanded) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Income",
                            style = MaterialTheme.typography.labelMedium,
                            color = SpendlyGray700,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Spacer(Modifier.size(8.dp))
                        SmallFloatingActionButton(
                            onClick = { fabExpanded = false; onAddIncome() },
                            containerColor = SpendlyGreen,
                            shape = CircleShape
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add income", tint = Color.White)
                        }
                    }
                    Spacer(Modifier.size(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Expense",
                            style = MaterialTheme.typography.labelMedium,
                            color = SpendlyGray700,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Spacer(Modifier.size(8.dp))
                        SmallFloatingActionButton(
                            onClick = { fabExpanded = false; onAddExpense() },
                            containerColor = SpendlyGreen,
                            shape = CircleShape
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add expense", tint = Color.White)
                        }
                    }
                    Spacer(Modifier.size(8.dp))
                }

                FloatingActionButton(
                    onClick = { fabExpanded = !fabExpanded },
                    containerColor = SpendlyGreen,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    }
}
