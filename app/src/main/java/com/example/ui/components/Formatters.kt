package com.example.ui.components

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatCurrency(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    formatter.maximumFractionDigits = if (amount % 1.0 == 0.0) 0 else 2
    formatter.minimumFractionDigits = if (amount % 1.0 == 0.0) 0 else 2
    return formatter.format(amount)
}

fun formatCurrencyExact(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    formatter.maximumFractionDigits = 2
    formatter.minimumFractionDigits = 2
    return formatter.format(amount)
}

fun formatOrderDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM d, yyyy", Locale.US)
    return sdf.format(Date(timestamp))
}
