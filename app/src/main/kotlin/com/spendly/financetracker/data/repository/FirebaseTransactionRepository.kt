package com.spendly.financetracker.data.repository

import android.content.Context
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.spendly.financetracker.data.firebase.FirebaseBootstrap
import com.spendly.financetracker.data.model.FinanceTransaction
import com.spendly.financetracker.data.model.TransactionDraft
import com.spendly.financetracker.data.model.TransactionType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseTransactionRepository(context: Context) : TransactionRepository {
    private val appContext = context.applicationContext

    override fun observeTransactions(userId: String): Flow<List<FinanceTransaction>> = callbackFlow {
        val firestore = firestoreOrNull()
        if (firestore == null) {
            close(IllegalStateException(FirebaseBootstrap.MISSING_CONFIG_MESSAGE))
            return@callbackFlow
        }

        val listener = transactionsCollection(firestore, userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val transactions = snapshot
                    ?.documents
                    ?.mapNotNull { it.toFinanceTransaction() }
                    .orEmpty()

                trySend(transactions)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun addTransaction(
        userId: String,
        draft: TransactionDraft
    ): Result<Unit> = runCatching {
        val firestore = firestoreOrNull() ?: error(FirebaseBootstrap.MISSING_CONFIG_MESSAGE)

        val data = mapOf(
            "title" to draft.title,
            "amountCents" to draft.amountCents,
            "type" to draft.type.name,
            "note" to draft.note,
            "ownerId" to userId,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        transactionsCollection(firestore, userId).add(data).await()
    }.map { Unit }

    override suspend fun deleteTransaction(userId: String, transactionId: String): Result<Unit> = runCatching {
        val firestore = firestoreOrNull() ?: error(FirebaseBootstrap.MISSING_CONFIG_MESSAGE)
        transactionsCollection(firestore, userId).document(transactionId).delete().await()
    }.map { Unit }

    private fun firestoreOrNull(): FirebaseFirestore? {
        if (!FirebaseBootstrap.ensureInitialized(appContext)) return null
        return runCatching { FirebaseFirestore.getInstance() }.getOrNull()
    }

    private fun transactionsCollection(firestore: FirebaseFirestore, userId: String) =
        firestore
            .collection("users")
            .document(userId)
            .collection("transactions")

    private fun DocumentSnapshot.toFinanceTransaction(): FinanceTransaction? {
        val title = getString("title")?.takeIf { it.isNotBlank() } ?: return null
        val amountCents = getLong("amountCents") ?: return null
        val type = getString("type")?.toTransactionTypeOrNull() ?: return null

        return FinanceTransaction(
            id = id,
            title = title,
            amountCents = amountCents,
            type = type,
            note = getString("note").orEmpty(),
            createdAtMillis = getTimestamp("createdAt")?.toDate()?.time ?: 0L
        )
    }

    private fun String.toTransactionTypeOrNull(): TransactionType? =
        runCatching { TransactionType.valueOf(this) }.getOrNull()
}
