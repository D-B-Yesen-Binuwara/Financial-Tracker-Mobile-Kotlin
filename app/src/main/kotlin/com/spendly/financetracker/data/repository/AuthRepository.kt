package com.spendly.financetracker.data.repository

import com.spendly.financetracker.data.model.UserSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isFirebaseConfigured: Boolean

    fun observeSession(): Flow<UserSession?>

    suspend fun signIn(email: String, password: String): Result<Unit>

    suspend fun createAccount(email: String, password: String): Result<Unit>

    fun signOut()
}
