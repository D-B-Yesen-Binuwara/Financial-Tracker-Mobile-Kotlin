package com.spendly.financetracker.ui.screen.goals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.spendly.financetracker.ui.components.GoalCard
import com.spendly.financetracker.ui.components.PrimaryGoalCard
import com.spendly.financetracker.ui.viewmodel.FinanceUiState

typealias OnAddGoal = () -> Unit

@Composable
fun GoalsScreen(state: FinanceUiState, onAddGoal: OnAddGoal) {
    val otherGoals = state.goals.filter { !it.isPrimary }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
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
