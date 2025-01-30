package com.chaitanya.mangashelf.utility

import java.util.Locale
import kotlin.math.ln
import kotlin.math.pow
import kotlin.text.*

fun Long.toYear(): Int {
    return java.util.Calendar.getInstance().apply {
        timeInMillis = this@toYear * 1000L
    }.get(java.util.Calendar.YEAR)
}

fun Long.getFormatedNumber(): String {
    if (this < 1000) return "" + this
    val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
    return String.format(Locale.getDefault(),"%.1f %c", this / 1000.0.pow(exp.toDouble()), "kMGTPE"[exp - 1])
}

fun Long.toReadableDate(): String {
    val sdf = java.text.SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val date = java.util.Date(this * 1000L)
    return sdf.format(date)
}
