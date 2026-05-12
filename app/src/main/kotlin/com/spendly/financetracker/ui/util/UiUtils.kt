package com.spendly.financetracker.ui.util

fun formatMoney(cents: Long): String {
    val sign = if (cents < 0L) "-" else ""
    val absolute = kotlin.math.abs(cents)
    val whole = absolute / 100L
    val fraction = (absolute % 100L).toString().padStart(2, '0')
    return "${sign}Rs $whole.$fraction"
}

fun formatPercent(value: Int): String = "$value%"
