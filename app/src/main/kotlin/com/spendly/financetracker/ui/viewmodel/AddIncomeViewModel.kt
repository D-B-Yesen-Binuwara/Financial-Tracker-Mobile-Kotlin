package com.spendly.financetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spendly.financetracker.data.model.TransactionDraft
import com.spendly.financetracker.data.model.TransactionType
import com.spendly.financetracker.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddIncomeUiState(
    val name: String = "",
    val amount: String = "",
    val note: String = "",
    val selectedSource: String = "Salary",
    val selectedDate: Long = System.currentTimeMillis(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

val incomeSources = listOf("Salary", "Freelance", "Crypto", "AdSense", "Other")

class AddIncomeViewModel(
    private val transactionRepository: TransactionRepository,
    private val userId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddIncomeUiState())
    val uiState: StateFlow<AddIncomeUiState> = _uiState.asStateFlow()

    fun onNameChanged(v: String) = _uiState.update { it.copy(name = v, error = null) }
    fun onAmountChanged(v: String) {
        if (v.isEmpty() || (v.all { it.isDigit() || it == '.' } && v.count { it == '.' } <= 1))
            _uiState.update { it.copy(amount = v, error = null) }
    }
    fun onNoteChanged(v: String) = _uiState.update { it.copy(note = v) }
    fun onSourceSelected(s: String) = _uiState.update { it.copy(selectedSource = s) }
    fun onDateSelected(ms: Long) = _uiState.update { it.copy(selectedDate = ms) }
    fun clearError() = _uiState.update { it.copy(error = null) }

    fun save() {
        val s = _uiState.value
        val amountCents = parseAmountCents(s.amount)
        if (s.name.isBlank()) { _uiState.update { it.copy(error = "Enter a name") }; return }
        if (amountCents == null || amountCents <= 0L) { _uiState.update { it.copy(error = "Enter a valid amount") }; return }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = transactionRepository.addTransaction(
                userId,
                TransactionDraft(
                    title = s.name.trim(),
                    amountCents = amountCents,
                    type = TransactionType.INCOME,
                    note = s.note.trim()
                )
            )
            _uiState.update {
                if (result.isSuccess) it.copy(isLoading = false, isSaved = true)
                else it.copy(isLoading = false, error = result.exceptionOrNull()?.message ?: "Failed to save")
            }
        }
    }

    private fun parseAmountCents(input: String): Long? {
        val n = input.trim()
        if (!Regex("^\\d+([.]\\d{1,2})?$").matches(n)) return null
        val parts = n.split(".")
        val whole = parts[0].toLongOrNull() ?: return null
        val cents = parts.getOrNull(1)?.padEnd(2, '0') ?: "00"
        return whole * 100L + (cents.toLongOrNull() ?: return null)
    }

    class Factory(
        private val transactionRepository: TransactionRepository,
        private val userId: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AddIncomeViewModel(transactionRepository, userId) as T
    }
}
