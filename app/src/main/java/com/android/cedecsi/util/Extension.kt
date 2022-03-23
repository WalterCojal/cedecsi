package com.android.cedecsi.util

import java.text.SimpleDateFormat
import java.util.*

fun getFormat(pattern: String, date: Date): String {
    return SimpleDateFormat(pattern, Locale("es", "PE")).apply {
        timeZone = TimeZone.getTimeZone("GMT-5")
    }.format(date)
}