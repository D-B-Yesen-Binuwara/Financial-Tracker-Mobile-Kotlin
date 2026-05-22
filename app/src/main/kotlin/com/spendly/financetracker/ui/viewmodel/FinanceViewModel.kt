package com.spendly.financetracker.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spendly.financetracker.data.firebase.FirebaseBootstrap
import com.spendly.financetracker.data.model.TransactionDraft
import com.spendly.financetracker.data.model.TransactionType
import com.spendly.financetracker.data.repository.AuthRepository
import com.spendly.financetracker.data.repository.FirebaseAuthRepository
import com.spendly.financetracker.data.repository.FirebaseTransactionRepository
import com.spendly.financetracker.data.repository.TransactionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FinanceViewModel(
    private val authRepository: AuthRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FinanceUiState())
    val uiState: StateFlow<FinanceUiState> = _uiState.asStateFlow()

    private var transactionsJob: Job? = null

    init {
        if (!authRepository.isFirebaseConfigured) {
            _uiState.update {
                it.copy(
                    isFirebaseConfigured = false,
                    isLoading = false,
                    message = FirebaseBootstrap.MISSING_CONFIG_MESSAGE
                )
            }
        } else {
            observeSession()
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, message = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, message = null) }
    }

    fun toggleAuthMode() {
        _uiState.update {
            it.copy(
                authMode = if (it.authMode == AuthMode.SIGN_IN) {
                    AuthMode.CREATE_ACCOUNT
                } else {
                    AuthMode.SIGN_IN
                },
                password = "",
                message = null
            )
        }
    }

    fun submitAuth() {
        val state = _uiState.value
        val email = state.email.trim()
        val password = state.password

        if (email.isBlank() || "@" !in email) {
            _uiState.update { it.copy(message = "Enter a valid email address.") }
            return
        }

        if (password.length < 6) {
            _uiState.update { it.copy(message = "Password must be at least 6 characters.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isBusy = true, message = null) }

            val result = if (state.authMode == AuthMode.SIGN_IN) {
                authRepository.signIn(email, password)
            } else {
                authRepository.createAccount(email, password)
            }

            _uiState.update {
                it.copy(
                    isBusy = false,
                    password = if (result.isSuccess) "" else it.password,
                    message = result.exceptionOrNull()?.userMessage()
                )
            }
        }
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun selectTransactionTab(tab: TransactionTab) {
        _uiState.update { it.copy(transactionTab = tab) }
    }

    fun addGoal() {
        _uiState.update {
            val nextIndex = it.goals.size + 1
            val newGoal = Goal(
                id = "goal-$nextIndex",
                title = "New Goal $nextIndex",
                targetCents = 120_000_00L,
                savedCents = 18_000_00L,
                dueDate = "Dec 2026",
                category = "Custom",
                isPrimary = false
            )

            it.copy(goals = it.goals + newGoal, message = "Added a new goal")
        }
    }

    fun signOut() {
        transactionsJob?.cancel()
        transactionsJob = null
        authRepository.signOut()
        _uiState.update { it.copy(transactions = emptyList(), currentTab = AppTab.HOME, transactionTab = TransactionTab.ALL) }
    }

    fun updateTransactionTitle(title: String) {
        _uiState.update { it.copy(transactionTitle = title, message = null) }
    }

    fun updateTransactionAmount(amount: String) {
        _uiState.update { it.copy(transactionAmount = amount, message = null) }
    }

    fun updateTransactionNote(note: String) {
        _uiState.update { it.copy(transactionNote = note, message = null) }
    }

    fun selectTransactionType(type: TransactionType) {
        _uiState.update { it.copy(transactionType = type, message = null) }
    }

    fun addTransaction() {
        val state = _uiState.value
        val session = state.session ?: return
        val amountCents = parseAmountCents(state.transactionAmount)

        if (state.transactionTitle.isBlank()) {
            _uiState.update { it.copy(message = "Enter a transaction title.") }
            return
        }

        if (amountCents == null || amountCents <= 0L) {
            _uiState.update { it.copy(message = "Enter a positive amount with up to 2 decimals.") }
            return
        }

        val draft = TransactionDraft(
            title = state.transactionTitle.trim(),
            amountCents = amountCents,
            type = state.transactionType,
            note = state.transactionNote.trim()
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isBusy = true, message = null) }
            val result = transactionRepository.addTransaction(session.uid, draft)

            _uiState.update {
                if (result.isSuccess) {
                    it.copy(
                        isBusy = false,
                        transactionTitle = "",
                        transactionAmount = "",
                        transactionNote = "",
                        message = "Transaction saved."
                    )
                } else {
                    it.copy(
                        isBusy = false,
                        message = result.exceptionOrNull()?.userMessage()
                    )
                }
            }
        }
    }

    fun deleteTransaction(transactionId: String) {
        val session = _uiState.value.session ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isBusy = true, message = null) }
            val result = transactionRepository.deleteTransaction(session.uid, transactionId)
            _uiState.update {
                it.copy(
                    isBusy = false,
                    message = if (result.isSuccess) "Transaction deleted." else result.exceptionOrNull()?.userMessage()
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }

    private fun observeSession() {
        viewModelScope.launch {
            authRepository
                .observeSession()
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = error.userMessage()
                        )
                    }
                }
                .collect { session ->
                    transactionsJob?.cancel()
                    transactionsJob = null

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            session = session,
                            transactions = emptyList()
                        )
                    }

                    if (session != null) {
                        observeTransactions(session.uid)
                    }
                }
        }
    }

    private fun observeTransactions(userId: String) {
        transactionsJob = viewModelScope.launch {
            transactionRepository
                .observeTransactions(userId)
                .catch { error ->
                    _uiState.update { it.copy(message = error.userMessage()) }
                }
                .collect { transactions ->
                    _uiState.update { it.copy(transactions = transactions) }
                }
        }
    }

    private fun parseAmountCents(input: String): Long? {
        val normalized = input.trim()
        if (!Regex("^\\d+([.]\\d{1,2})?$").matches(normalized)) return null

        val parts = normalized.split(".")
        val whole = parts.getOrNull(0)?.toLongOrNull() ?: return null
        val cents = parts
            .getOrNull(1)
            ?.padEnd(2, '0')
            ?: "00"

        return whole * 100L + (cents.toLongOrNull() ?: return null)
    }

    private fun Throwable.userMessage(): String =
        message?.takeIf { it.isNotBlank() } ?: "Something went wrong. Please try again."

    class Factory(context: Context) : ViewModelProvider.Factory {
        private val appContext = context.applicationContext

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
                return FinanceViewModel(
                    authRepository = FirebaseAuthRepository(appContext),
                    transactionRepository = FirebaseTransactionRepository(appContext)
                ) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
