package com.blank.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun parseLocalDateTime(input: String): LocalDateTime {
    try {
        return LocalDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME)
    } catch (e: Exception) {
        throw IllegalArgumentException("Wrong dateTime format provided.")
    }
}