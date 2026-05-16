package com.spendly.financetracker.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.spendly.financetracker.data.model.TransactionType
import com.spendly.financetracker.ui.components.AppBottomNavigation
import com.spendly.financetracker.ui.components.OnTabSelected
import com.spendly.financetracker.ui.screen.analytics.AnalyticsScreen
import com.spendly.financetracker.ui.screen.goals.GoalsScreen
import com.spendly.financetracker.ui.screen.goals.OnAddGoal
import com.spendly.financetracker.ui.screen.home.HomeScreen
import com.spendly.financetracker.ui.screen.profile.ProfileScreen
import com.spendly.financetracker.ui.screen.transactions.OnTransactionTabSelected
import com.spendly.financetracker.ui.screen.transactions.TransactionsScreen
import com.spendly.financetracker.ui.viewmodel.AppTab
import com.spendly.financetracker.ui.viewmodel.FinanceUiState

typealias OnSignOut = () -> Unit

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
