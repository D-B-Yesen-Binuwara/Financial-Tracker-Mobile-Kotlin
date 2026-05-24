package com.spendly.financetracker.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.spendly.financetracker.data.firebase.FirebaseBootstrap
import com.spendly.financetracker.data.model.UserSession
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(context: Context) : AuthRepository {
    private val appContext = context.applicationContext //firebase auth repo

    override val isFirebaseConfigured: Boolean
        get() = FirebaseBootstrap.isConfigured(appContext)

    override fun observeSession(): Flow<UserSession?> = callbackFlow {
        val auth = authOrNull()
        if (auth == null) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.toSession())
        }

        auth.addAuthStateListener(listener)
        trySend(auth.currentUser?.toSession())

        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        val auth = authOrError()
        auth.signInWithEmailAndPassword(email.trim(), password).await()
    }.map { Unit }

    override suspend fun createAccount(email: String, password: String): Result<Unit> = runCatching {
        val auth = authOrError()
        auth.createUserWithEmailAndPassword(email.trim(), password).await()
    }.map { Unit }

    override fun signOut() {
        authOrNull()?.signOut()
    }

    private fun authOrError(): FirebaseAuth =
        authOrNull() ?: error(FirebaseBootstrap.MISSING_CONFIG_MESSAGE)

    private fun authOrNull(): FirebaseAuth? {
        if (!FirebaseBootstrap.ensureInitialized(appContext)) return null
        return runCatching { FirebaseAuth.getInstance() }.getOrNull()
    }

    private fun FirebaseUser.toSession(): UserSession =
        UserSession(
            uid = uid,
            email = email
        )
}
