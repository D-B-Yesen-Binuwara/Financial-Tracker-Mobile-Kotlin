package com.spendly.financetracker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spendly.financetracker.ui.screen.AuthScreen
import com.spendly.financetracker.ui.screen.FirebaseSetupScreen
import com.spendly.financetracker.ui.screen.MainAppScreen
import com.spendly.financetracker.ui.viewmodel.FinanceViewModel

@Composable
fun FinanceTrackerApp(viewModel: FinanceViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        val message = state.message ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        viewModel.clearMessage()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        when {
            !state.isFirebaseConfigured -> FirebaseSetupScreen(contentPadding = padding)
            state.isLoading -> LoadingScreen(contentPadding = padding)
            state.session == null -> AuthScreen(
                state = state,
                contentPadding = padding,
                onEmailChange = viewModel::updateEmail,
                onPasswordChange = viewModel::updatePassword,
                onSubmit = viewModel::submitAuth,
                onToggleMode = viewModel::toggleAuthMode
            )
            else -> MainAppScreen(
                state = state,
                contentPadding = padding,
                onTabSelected = viewModel::selectTab,
                onTransactionTabSelected = viewModel::selectTransactionTab,
                onAddGoal = viewModel::addGoal,
                onSignOut = viewModel::signOut,
                onTitleChange = viewModel::updateTransactionTitle,
                onAmountChange = viewModel::updateTransactionAmount,
                onNoteChange = viewModel::updateTransactionNote,
                onTypeChange = viewModel::selectTransactionType,
                onAddTransaction = viewModel::addTransaction,
                onDeleteTransaction = viewModel::deleteTransaction
            )
        }
    }
}

@Composable
private fun LoadingScreen(contentPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
