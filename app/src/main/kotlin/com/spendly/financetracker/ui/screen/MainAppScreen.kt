package com.spendly.financetracker.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.spendly.financetracker.data.model.TransactionType
import com.spendly.financetracker.ui.components.AppBottomNavigation
import com.spendly.financetracker.ui.components.OnTabSelected
import com.spendly.financetracker.ui.screen.analytics.AnalyticsScreen
import com.spendly.financetracker.ui.screen.goals.GoalsScreen
import com.spendly.financetracker.ui.screen.goals.OnAddGoal
import com.spendly.financetracker.ui.screen.goals.PrimaryGoalDetailScreen
import com.spendly.financetracker.ui.screen.home.HomeScreen
import com.spendly.financetracker.ui.screen.profile.ProfileScreen
import com.spendly.financetracker.ui.screen.transactions.AddTransactionScreen
import com.spendly.financetracker.ui.screen.transactions.OnTransactionTabSelected
import com.spendly.financetracker.ui.screen.transactions.TransactionsScreen
import com.spendly.financetracker.ui.viewmodel.AppTab
import com.spendly.financetracker.ui.viewmodel.FinanceUiState

typealias OnSignOut = () -> Unit

private enum class AppFlow {
    ADD_TRANSACTION,
    GOAL_DETAIL
}

@Composable
fun MainAppScreen(
    state: FinanceUiState,
    contentPadding: PaddingValues,
    onTabSelected: OnTabSelected,
    onTransactionTabSelected: OnTransactionTabSelected,
    onAddGoal: OnAddGoal,
    onSignOut: OnSignOut,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onAddTransaction: () -> Unit
) {
    var activeFlow by rememberSaveable { mutableStateOf<AppFlow?>(null) }
    var closeAfterSave by rememberSaveable { mutableStateOf(false) }
    var observedSaveWork by rememberSaveable { mutableStateOf(false) }

    fun openAddTransaction(type: TransactionType) {
        onTypeChange(type)
        closeAfterSave = false
        observedSaveWork = false
        activeFlow = AppFlow.ADD_TRANSACTION
    }

    LaunchedEffect(activeFlow, state.isBusy, state.transactionTitle, state.transactionAmount) {
        if (activeFlow == AppFlow.ADD_TRANSACTION && closeAfterSave) {
            if (state.isBusy) observedSaveWork = true
            if (
                observedSaveWork &&
                !state.isBusy &&
                state.transactionTitle.isBlank() &&
                state.transactionAmount.isBlank()
            ) {
                activeFlow = null
                closeAfterSave = false
                observedSaveWork = false
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (activeFlow == null) {
                AppBottomNavigation(currentTab = state.currentTab, onTabSelected = onTabSelected)
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(contentPadding)
        ) {
            when (activeFlow) {
                AppFlow.ADD_TRANSACTION -> AddTransactionScreen(
                    state = state,
                    onClose = {
                        activeFlow = null
                        closeAfterSave = false
                        observedSaveWork = false
                    },
                    onTitleChange = onTitleChange,
                    onAmountChange = onAmountChange,
                    onNoteChange = onNoteChange,
                    onTypeChange = onTypeChange,
                    onSave = {
                        closeAfterSave = true
                        observedSaveWork = false
                        onAddTransaction()
                    }
                )
                AppFlow.GOAL_DETAIL -> PrimaryGoalDetailScreen(
                    state = state,
                    onBack = { activeFlow = null }
                )
                null -> {
                    when (state.currentTab) {
                        AppTab.HOME -> HomeScreen(
                            state = state,
                            onOpenProfile = { onTabSelected(AppTab.PROFILE) },
                            onOpenTransactions = { onTabSelected(AppTab.TRANSACTIONS) },
                            onOpenGoal = { activeFlow = AppFlow.GOAL_DETAIL },
                            onAddExpense = { openAddTransaction(TransactionType.EXPENSE) }
                        )
                        AppTab.TRANSACTIONS -> TransactionsScreen(
                            state = state,
                            onTransactionTabSelected = onTransactionTabSelected,
                            onAddExpense = { openAddTransaction(TransactionType.EXPENSE) },
                            onAddIncome = { openAddTransaction(TransactionType.INCOME) }
                        )
                        AppTab.GOALS -> GoalsScreen(
                            state = state,
                            onAddGoal = onAddGoal,
                            onGoalSelected = { activeFlow = AppFlow.GOAL_DETAIL }
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
    }
}
