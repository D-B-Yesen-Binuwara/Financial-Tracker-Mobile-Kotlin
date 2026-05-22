package com.spendly.financetracker.ui.screen.goals

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendly.financetracker.ui.components.NoGoalState
import com.spendly.financetracker.ui.theme.SpendlyGray100
import com.spendly.financetracker.ui.theme.SpendlyGray300
import com.spendly.financetracker.ui.theme.SpendlyGray500
import com.spendly.financetracker.ui.theme.SpendlyGray700
import com.spendly.financetracker.ui.theme.SpendlyGray900
import com.spendly.financetracker.ui.theme.SpendlyGreen
import com.spendly.financetracker.ui.theme.SpendlyGreenDark
import com.spendly.financetracker.ui.theme.SpendlyGreenLight
import com.spendly.financetracker.ui.util.formatMoney
import com.spendly.financetracker.ui.viewmodel.FinanceUiState
import com.spendly.financetracker.ui.viewmodel.Goal
import com.spendly.financetracker.ui.viewmodel.GoalDraft

typealias OnAddGoal = () -> Unit
typealias OnGoalSelected = (String) -> Unit
typealias OnSaveGoal = (GoalDraft) -> Boolean

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    state: FinanceUiState,
    onAddGoal: OnAddGoal,
    onGoalSelected: OnGoalSelected
) {
    val otherGoals = state.goals.filter { !it.isPrimary }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Goal Tracker", fontWeight = FontWeight.Bold) })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddGoal,
                modifier = Modifier
                    .size(56.dp)
                    .offset(y = 8.dp),
                shape = CircleShape,
                containerColor = SpendlyGreen,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add new goal")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 104.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.goals.isEmpty()) {
                item {
                    NoGoalState(onSetGoal = onAddGoal)
                }
            } else {
                state.primaryGoal?.let { goal ->
                    item {
                        Text(
                            "Primary Goal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item {
                        PrimaryGoalCard(
                            goal = goal,
                            onClick = { onGoalSelected(goal.id) }
                        )
                    }
                }

                if (otherGoals.isNotEmpty()) {
                    item {
                        Text(
                            "Other Goals",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(otherGoals, key = { it.id }) { goal ->
                        OtherGoalRow(
                            goal = goal,
                            onClick = { onGoalSelected(goal.id) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalScreen(
    onBack: () -> Unit,
    onSave: OnSaveGoal
) {
    var goalName by rememberSaveable { mutableStateOf("") }
    var status by rememberSaveable { mutableStateOf("") }
    var targetAmount by rememberSaveable { mutableStateOf("") }
    var targetDate by rememberSaveable { mutableStateOf("") }
    var initialSaved by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("Add New Goal", fontWeight = FontWeight.Bold) },
                actions = {
                    Button(
                        onClick = {
                            onSave(
                                GoalDraft(
                                    title = goalName,
                                    status = status,
                                    targetAmount = targetAmount,
                                    targetDate = targetDate,
                                    initialSaved = initialSaved
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SpendlyGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .height(40.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 0.dp)
                    ) {
                        Text("Save", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(SpendlyGreenLight, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.TrackChanges,
                        contentDescription = null,
                        tint = SpendlyGreen
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Set a Target",
                        style = MaterialTheme.typography.titleMedium,
                        color = SpendlyGray900,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Plan your next big purchase",
                        style = MaterialTheme.typography.bodySmall,
                        color = SpendlyGray500
                    )
                }
            }

            GoalFormField(
                label = "Goal Name",
                value = goalName,
                onValueChange = { goalName = it },
                placeholder = "e.g. Dream Vacation"
            )
            GoalFormField(
                label = "Status",
                value = status,
                onValueChange = { status = it },
                placeholder = "e.g. On track"
            )
            GoalFormField(
                label = "Target Amount",
                value = targetAmount,
                onValueChange = { targetAmount = it },
                placeholder = "0",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            GoalFormField(
                label = "Target Date",
                value = targetDate,
                onValueChange = { targetDate = it },
                placeholder = "e.g. Dec 2026"
            )
            GoalFormField(
                label = "Initial Saved (Optional)",
                value = initialSaved,
                onValueChange = { initialSaved = it },
                placeholder = "0",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }
    }
}

@Composable
private fun GoalFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = SpendlyGray900,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = SpendlyGray500) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = keyboardOptions
        )
    }
}

@Composable
private fun PrimaryGoalCard(
    goal: Goal,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = SpendlyGreen),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    goal.title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    color = SpendlyGreenDark,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                        Text(goal.status.uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }
                }
            }

            Text(
                "${goal.dueDate} - ${formatMoney(goal.remainingCents)} remaining",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.82f)
            )

            LinearProgressIndicator(
                progress = { goal.progressPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f)
            )

            Row {
                Text(
                    formatMoney(goal.savedCents),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    " / ${formatMoney(goal.targetCents)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun OtherGoalRow(
    goal: Goal,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, SpendlyGray300),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(SpendlyGreenLight, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (goal.title.contains("mac", ignoreCase = true)) Icons.Default.Laptop else Icons.Default.Flag,
                    contentDescription = null,
                    tint = SpendlyGreen
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row {
                    Text(goal.title, modifier = Modifier.weight(1f), color = SpendlyGray900, fontWeight = FontWeight.Bold)
                    Text("${goal.progressPercent}%", color = SpendlyGreen, fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(
                    progress = { goal.progressPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = SpendlyGreen,
                    trackColor = SpendlyGreenLight
                )
                Text(
                    "${formatMoney(goal.savedCents)} / ${formatMoney(goal.targetCents)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = SpendlyGray500
                )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SpendlyGray700)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailScreen(
    state: FinanceUiState,
    goalId: String?,
    onBack: () -> Unit
) {
    val goal = goalId?.let { selectedGoalId ->
        state.goals.firstOrNull { it.id == selectedGoalId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text(goal?.title ?: "Goal Details", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        if (goal == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                NoGoalState(onSetGoal = onBack)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    PrimaryGoalCard(goal = goal, onClick = {})
                }
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(0.5.dp, SpendlyGray300),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DetailRow("Saved", formatMoney(goal.savedCents))
                            DetailRow("Remaining", formatMoney(goal.remainingCents))
                            DetailRow("Target date", goal.dueDate)
                            DetailRow("Status", goal.status)
                            DetailRow("Category", goal.category)
                            DetailRow("Required monthly", formatMoney(goal.remainingCents / 12L))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = SpendlyGray500)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = SpendlyGray900, fontWeight = FontWeight.Bold)
    }
}
