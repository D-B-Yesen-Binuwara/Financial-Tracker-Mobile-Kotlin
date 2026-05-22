package com.spendly.financetracker.ui.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Utility functions for formatting and displaying data in the UI.
 * Includes helpers for currency formatting, date formatting, and user-related text generation.
 */

/**
 * Formats a monetary value given in cents to a user-friendly string.
 * @param cents The monetary amount in cents (can be negative)
 * @return Formatted string in the format "LKR X,XXX.XX" with optional negative sign
 */
fun formatMoney(cents: Long): String {
    val sign = if (cents < 0L) "-" else ""
    val absolute = kotlin.math.abs(cents)
    val amount = absolute / 100.0
    val formatted = DecimalFormat("#,##0.00").format(amount)
    return "${sign}LKR $formatted"
}

/**
 * Formats a percentage value as a string.
 * @param value The percentage value (e.g., 50 for 50%)
 * @return Formatted percentage string with the '%' symbol
 */
fun formatPercent(value: Int): String = "$value%"

/**
 * Formats a date in short format (e.g., "Jan 15").
 * Shows "Today" for invalid or zero timestamps.
 * @param timeMillis The timestamp in milliseconds (0 or negative = "Today")
 * @return Formatted date string in "MMM d" format or "Today"
 */
fun formatDateShort(timeMillis: Long): String {
    if (timeMillis <= 0L) return "Today"
    return SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timeMillis))
}

/**
 * Formats a date in long format (e.g., "Jan 15, 2024").
 * Shows "Today" for invalid or zero timestamps.
 * @param timeMillis The timestamp in milliseconds (0 or negative = "Today")
 * @return Formatted date string in "MMM d, yyyy" format or "Today"
 */
fun formatDateFull(timeMillis: Long): String {
    if (timeMillis <= 0L) return "Today"
    return SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(timeMillis))
}

/**
 * Gets the current month and year as a formatted label.
 * @return Current month and year in "MMMM yyyy" format (e.g., "May 2024")
 */
fun currentMonthLabel(): String =
    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())

/**
 * Generates a greeting message based on the current time of day.
 * @return One of: "Good morning" (5am-11am), "Good afternoon" (12pm-4pm), or "Good evening" (else)
 */
fun greetingForNow(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        else -> "Good evening"
    }
}

/**
 * Extracts initials from an email address to create a display initials string.
 * Splits the local part (before @) by delimiters (., _, -, space) and takes first letter of first 2 parts.
 * @param email The email address (can be null)
 * @return User initials (up to 2 characters), "U" if email is null or blank
 */
fun initialsFromEmail(email: String?): String {
    val trimmed = email.orEmpty().trim()
    if (trimmed.isBlank()) return "U"
    val localPart = trimmed.substringBefore("@")
    return localPart
        .split('.', '_', '-', ' ')
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.take(1).uppercase(Locale.getDefault()) }
        .ifBlank { trimmed.take(1).uppercase(Locale.getDefault()) }
}

/**
 * Generates a display name from an email address.
 * Extracts the local part (before @), splits by delimiters (., _, -),
 * and capitalizes each part to create a proper name format.
 * @param email The email address (can be null)
 * @return Display name with proper capitalization, or "Spendly User" if email is null/blank
 */
fun displayNameFromEmail(email: String?): String {
    val localPart = email.orEmpty().substringBefore("@").ifBlank { "Spendly User" }
    return localPart
        .split('.', '_', '-')
        .filter { it.isNotBlank() }
        .joinToString(" ") { part ->
            part.replaceFirstChar { char ->
                if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
            }
        }
        .ifBlank { "Spendly User" }
}
