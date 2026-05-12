package com.spendly.financetracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.spendly.financetracker.data.model.FinanceTransaction
import com.spendly.financetracker.data.model.TransactionType
import com.spendly.financetracker.ui.viewmodel.AppTab
import com.spendly.financetracker.ui.viewmodel.FinanceUiState
import com.spendly.financetracker.ui.viewmodel.TransactionTab
import com.spendly.financetracker.ui.util.formatMoney
import com.spendly.financetracker.ui.util.formatPercent

typealias OnTabSelected = (AppTab) -> Unit

typealias OnTransactionTabSelected = (TransactionTab) -> Unit

typealias OnGoalAction = () -> Unit

@Composable
fun MainAppScreen(
    state: FinanceUiState,
    contentPadding: PaddingValues,
    onTabSelected: OnTabSelected,
    onTransactionTabSelected: OnTransactionTabSelected,
    onAddGoal: OnGoalAction,
    onSignOut: OnGoalAction,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onAddTransaction: OnGoalAction
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AppBottomNavigation(currentTab = state.currentTab, onTabSelected = onTabSelected)
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(contentPadding)
        ) {
            when (state.currentTab) {
                AppTab.HOME -> HomeScreen(
                    state = state,
                    onSignOut = onSignOut
                )
                AppTab.TRANSACTIONS -> TransactionsScreen(
                    state = state,
                    onTransactionTabSelected = onTransactionTabSelected
                )
                AppTab.GOALS -> GoalsScreen(
                    state = state,
                    onAddGoal = onAddGoal
                )
                AppTab.ANALYTICS -> AnalyticsScreen(state = state)
                AppTab.PROFILE -> ProfileScreen(
                    state = state,
                    onSignOut = onSignOut
                )
            }
        }
    }
}

@Composable
private fun AppBottomNavigation(currentTab: AppTab, onTabSelected: OnTabSelected) {
    val items = listOf(
        AppTab.HOME,
        AppTab.TRANSACTIONS,
        AppTab.GOALS,
        AppTab.ANALYTICS,
        AppTab.PROFILE
    )

    NavigationBar {
        items.forEach { tab ->
            NavigationBarItem(
                selected = currentTab == tab,
                onClick = { onTabSelected(tab) },
                icon = { BottomNavIcon(tab = tab, selected = currentTab == tab) },
                label = { Text(tab.name.lowercase().replaceFirstChar { it.uppercase() }) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                )
            )
        }
    }
}

@Composable
private fun BottomNavIcon(tab: AppTab, selected: Boolean) {
    val label = when (tab) {
        AppTab.HOME -> "H"
        AppTab.TRANSACTIONS -> "T"
        AppTab.GOALS -> "G"
        AppTab.ANALYTICS -> "A"
        AppTab.PROFILE -> "P"
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
                if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun HomeScreen(state: FinanceUiState, onSignOut: OnGoalAction) {
    val recentTransactions = state.transactions
        .sortedByDescending { it.createdAtMillis }
        .take(4)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(18.dp)
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

@Composable
private fun HeaderSection(title: String, subtitle: String, onSignOut: OnGoalAction) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        TextButton(onClick = onSignOut) {
            Text(text = "Sign out")
        }
    }
}

@Composable
private fun SummaryPanel(
    incomeCents: Long,
    expenseCents: Long,
    savingsCents: Long,
    savingsRate: Int
) {
    Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard(label = "Income", amount = formatMoney(incomeCents), modifier = Modifier.weight(1f))
            SummaryCard(label = "Expenses", amount = formatMoney(expenseCents), modifier = Modifier.weight(1f))
        }

        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Net savings",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatMoney(savingsCents),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = formatPercent(savingsRate),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                LinearProgressIndicator(
                    progress = { savingsRate / 100f },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(label: String, amount: String, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = amount,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun PrimaryGoalCard(goal: com.spendly.financetracker.ui.viewmodel.Goal) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = goal.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Target: ${goal.dueDate}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "ON TRACK",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            LinearProgressIndicator(
                progress = { goal.progressPercent / 100f },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Saved ${formatMoney(goal.savedCents)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${goal.progressPercent}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun TransactionListItem(transaction: FinanceTransaction) {
    val amountPrefix = if (transaction.type == TransactionType.INCOME) "+" else "-"
    val amountColor = if (transaction.type == TransactionType.INCOME) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = transaction.note.ifBlank { transaction.type.name.lowercase().replaceFirstChar { it.uppercase() } },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = "$amountPrefix${formatMoney(transaction.amountCents)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = amountColor,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun TransactionsScreen(
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
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
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

@Composable
private fun GoalsScreen(state: FinanceUiState, onAddGoal: OnGoalAction) {
    val otherGoals = state.goals.filter { !it.isPrimary }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Goals",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Track your primary target and other savings goals.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(onClick = onAddGoal) {
                    Text("Add goal")
                }
            }
        }

        item {
            state.primaryGoal?.let { primaryGoal ->
                PrimaryGoalCard(goal = primaryGoal)
            }
        }

        item {
            Text(
                text = "Other goals",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (otherGoals.isEmpty()) {
            item {
                Text(
                    text = "You have no additional goals yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(otherGoals) { goal ->
                GoalCard(goal = goal)
            }
        }
    }
}

@Composable
private fun GoalCard(goal: com.spendly.financetracker.ui.viewmodel.Goal) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = goal.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${formatMoney(goal.savedCents)} of ${formatMoney(goal.targetCents)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "${goal.progressPercent}%",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            LinearProgressIndicator(progress = { goal.progressPercent / 100f }, modifier = Modifier.fillMaxWidth())
            Text(
                text = "Remaining: ${formatMoney(goal.remainingCents)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AnalyticsScreen(state: FinanceUiState) {
    val monthlyData = listOf(12, 44, 34, 76, 92)
    val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Analytics",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
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
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Monthly savings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        monthlyData.forEachIndexed { index, value ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.16f),
                                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                                    )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(value / 100f)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                                        )
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                    ) {
                        monthNames.forEach { month ->
                            Text(
                                text = month,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Insights",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
                    Text("Your savings rate is ${state.savingsRate}% compared to last month.")
                    Text("Primary goal progress is ${state.primaryGoal?.progressPercent ?: 0}%.")
                    Text("You have ${state.transactions.size} transactions tracked.")
                }
            }
        }
    }
}

@Composable
private fun ProfileScreen(state: FinanceUiState, onSignOut: OnGoalAction) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        }

        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.session?.email?.firstOrNull()?.uppercase() ?: "U",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Column {
                        Text(
                            text = state.session?.email.orEmpty(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Member since today",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                ProfileStat(label = "Goals", value = state.goals.size.toString())
                ProfileStat(label = "Transactions", value = state.transactions.size.toString())
                ProfileStat(label = "Savings", value = formatPercent(state.savingsRate))
            }
        }

        item {
            Button(onClick = onSignOut, modifier = Modifier.fillMaxWidth()) {
                Text("Sign out")
            }
        }
    }
}

@Composable
private fun ProfileStat(label: String, value: String) {
    ElevatedCard(modifier = Modifier.widthIn(min = 96.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
