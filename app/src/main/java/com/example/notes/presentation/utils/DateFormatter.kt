package com.example.notes.presentation.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import kotlin.text.compareTo

object DateFormatter {
    private val millisInHour: Long = TimeUnit.HOURS.toMillis(1)
    private val millisInDay: Long = TimeUnit.DAYS.toMillis(1)
    private val formatter: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT)
    fun formatDateToString(timestamp: Long): String {
        val now: Long = System.currentTimeMillis()
        val diff: Long = now - timestamp
        return when {
            diff < millisInHour -> "Just now"
            diff < millisInDay -> {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                "$hours h ago"
            }
            else ->{
            formatter.format(timestamp)
            }
        }
    }
}
