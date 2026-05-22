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
import java.util.UUID

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

    fun addGoal(draft: GoalDraft): String? {
        val title = draft.title.trim()
        val status = draft.status.trim().ifBlank { "Not Started" }
        val dueDate = draft.targetDate.trim()
        val targetCents = parseAmountCents(draft.targetAmount)
        val savedCents = if (draft.initialSaved.isBlank()) {
            0L
        } else {
            parseAmountCents(draft.initialSaved)
        }

        if (title.isBlank()) {
            _uiState.update { it.copy(message = "Enter a goal name.") }
            return null
        }

        if (targetCents == null || targetCents <= 0L) {
            _uiState.update { it.copy(message = "Enter a positive target amount.") }
            return null
        }

        if (dueDate.isBlank()) {
            _uiState.update { it.copy(message = "Enter a target date.") }
            return null
        }

        if (savedCents == null || savedCents < 0L) {
            _uiState.update { it.copy(message = "Enter a valid initial saved amount.") }
            return null
        }

        val newGoalId = "custom-goal-${UUID.randomUUID()}"
        val newGoal = Goal(
            id = newGoalId,
            title = title,
            targetCents = targetCents,
            savedCents = savedCents,
            dueDate = dueDate,
            category = "Custom",
            status = status,
            isPrimary = false
        )

        _uiState.update {
            it.copy(goals = it.goals + newGoal, message = "Added a new goal")
        }

        return newGoal.id
    }

    fun updateGoal(goalId: String, draft: GoalDraft): Boolean {
        val title = draft.title.trim()
        val status = draft.status.trim().ifBlank { "Not Started" }
        val dueDate = draft.targetDate.trim()
        val targetCents = parseAmountCents(draft.targetAmount)

        if (title.isBlank()) {
            _uiState.update { it.copy(message = "Enter a goal name.") }
            return false
        }

        if (targetCents == null || targetCents <= 0L) {
            _uiState.update { it.copy(message = "Enter a positive target amount.") }
            return false
        }

        if (dueDate.isBlank()) {
            _uiState.update { it.copy(message = "Enter a target date.") }
            return false
        }

        if (_uiState.value.goals.none { it.id == goalId }) {
            _uiState.update { it.copy(message = "Goal not found.") }
            return false
        }

        _uiState.update { state ->
            val updatedGoals = state.goals.map { goal ->
                if (goal.id == goalId) {
                    goal.copy(
                        title = title,
                        targetCents = targetCents,
                        dueDate = dueDate,
                        status = status
                    )
                } else {
                    goal
                }
            }

            state.copy(goals = updatedGoals, message = "Goal updated.")
        }

        return true
    }

    fun deleteGoal(goalId: String) {
        if (_uiState.value.goals.none { it.id == goalId }) {
            _uiState.update { it.copy(message = "Goal not found.") }
            return
        }

        _uiState.update { state ->
            val updated = state.goals.filterNot { it.id == goalId }
            state.copy(goals = updated, message = "Goal deleted.")
        }
    }

    fun addGoalSavings(goalId: String, amountInput: String): Boolean {
        val amountCents = parseAmountCents(amountInput)

        if (amountCents == null || amountCents <= 0L) {
            _uiState.update { it.copy(message = "Enter a positive savings amount.") }
            return false
        }

        if (_uiState.value.goals.none { it.id == goalId }) {
            _uiState.update { it.copy(message = "Goal not found.") }
            return false
        }

        _uiState.update { state ->
            val updatedGoals = state.goals.map { goal ->
                if (goal.id == goalId) {
                    goal.copy(savedCents = goal.savedCents + amountCents)
                } else {
                    goal
                }
            }

            state.copy(
                goals = updatedGoals,
                message = "Savings added."
            )
        }

        return true
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
