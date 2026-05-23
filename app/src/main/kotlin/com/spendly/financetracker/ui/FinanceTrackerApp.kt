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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.spendly.financetracker.data.repository.AuthRepository
import com.spendly.financetracker.data.repository.TransactionRepository
import com.spendly.financetracker.ui.components.AppBottomNavigation
import com.spendly.financetracker.ui.navigation.Screen
import com.spendly.financetracker.ui.navigation.bottomNavRoutes
import com.spendly.financetracker.ui.screen.AuthScreen
import com.spendly.financetracker.ui.screen.CreateAccountScreen
import com.spendly.financetracker.ui.screen.FirebaseSetupScreen
import com.spendly.financetracker.ui.screen.analytics.AnalyticsScreen
import com.spendly.financetracker.ui.screen.goals.GoalsScreen
import com.spendly.financetracker.ui.screen.home.HomeScreen
import com.spendly.financetracker.ui.screen.profile.ProfileScreen
import com.spendly.financetracker.ui.screen.transactions.AddExpenseScreen
import com.spendly.financetracker.ui.screen.transactions.AddIncomeScreen
import com.spendly.financetracker.ui.screen.transactions.TransactionsScreen
import com.spendly.financetracker.ui.viewmodel.AppTab
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

    when {
        !state.isFirebaseConfigured -> Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
            FirebaseSetupScreen(contentPadding = padding)
        }
        state.isLoading -> Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
            LoadingScreen(padding)
        }
        state.session == null -> {
            val authNavController = rememberNavController()
            Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
                NavHost(
                    navController = authNavController,
                    startDestination = Screen.Auth.route
                ) {
                    composable(Screen.Auth.route) {
                        AuthScreen(
                            state = state,
                            contentPadding = padding,
                            onEmailChange = viewModel::updateEmail,
                            onPasswordChange = viewModel::updatePassword,
                            onSubmit = viewModel::submitAuth,
                            onToggleMode = viewModel::toggleAuthMode,
                            onCreateAccount = { authNavController.navigate(Screen.CreateAccount.route) }
                        )
                    }

                    composable(Screen.CreateAccount.route) {
                        CreateAccountScreen(
                            authRepository = viewModel.authRepository,
                            contentPadding = padding,
                            onBack = { authNavController.popBackStack() }
                        )
                    }
                }
            }
        }
        else -> {
            val session = state.session!!
            val navController = rememberNavController()
            val currentBackStack by navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStack?.destination?.route

            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    if (currentRoute in bottomNavRoutes) {
                        val currentTab = when (currentRoute) {
                            Screen.Home.route -> AppTab.HOME
                            Screen.Events.route -> AppTab.TRANSACTIONS
                            Screen.Analytics.route -> AppTab.ANALYTICS
                            Screen.Goals.route -> AppTab.GOALS
                            Screen.Profile.route -> AppTab.PROFILE
                            else -> AppTab.HOME
                        }
                        AppBottomNavigation(
                            currentTab = currentTab,
                            onTabSelected = { tab ->
                                val route = when (tab) {
                                    AppTab.HOME -> Screen.Home.route
                                    AppTab.TRANSACTIONS -> Screen.Events.route
                                    AppTab.ANALYTICS -> Screen.Analytics.route
                                    AppTab.GOALS -> Screen.Goals.route
                                    AppTab.PROFILE -> Screen.Profile.route
                                }
                                if (currentRoute != route) {
                                    navController.navigate(route) {
                                        popUpTo(Screen.Home.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route,
                    modifier = Modifier.padding(padding)
                ) {
                    composable(Screen.Home.route) {
                        HomeScreen(
                            state = state,
                            onOpenProfile = { navController.navigate(Screen.Profile.route) },
                            onOpenTransactions = { navController.navigate(Screen.Events.route) },
                            onOpenGoal = { navController.navigate(Screen.Goals.route) },
                            onAddExpense = { navController.navigate(Screen.AddExpense.route) }
                        )
                    }

                    composable(Screen.Events.route) {
                        TransactionsScreen(
                            navController = navController,
                            transactionRepository = viewModel.transactionRepository,
                            userId = session.uid
                        )
                    }

                    composable(Screen.Analytics.route) {
                        AnalyticsScreen(state = state)
                    }

                    composable(Screen.Goals.route) {
                        GoalsScreen(
                            state = state,
                            onAddGoal = viewModel::addGoal,
                            onGoalSelected = {}
                        )
                    }

                    composable(Screen.Profile.route) {
                        ProfileScreen(
                            state = state,
                            onSignOut = {
                                viewModel.signOut()
                            }
                        )
                    }

                    composable(
                        route = Screen.AddIncome.routeWithArgs,
                        arguments = listOf(navArgument(Screen.AddIncome.ARG_ID) {
                            type = NavType.StringType; nullable = true; defaultValue = null
                        })
                    ) {
                        AddIncomeScreen(
                            transactionRepository = viewModel.transactionRepository,
                            userId = session.uid,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = Screen.AddExpense.routeWithArgs,
                        arguments = listOf(navArgument(Screen.AddExpense.ARG_ID) {
                            type = NavType.StringType; nullable = true; defaultValue = null
                        })
                    ) {
                        AddExpenseScreen(
                            transactionRepository = viewModel.transactionRepository,
                            userId = session.uid,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen(contentPadding: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize().padding(contentPadding),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}
