package com.android.cedecsi.util

import android.app.Activity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.text.SimpleDateFormat
import java.util.*

fun getFormat(pattern: String, date: Date): String {
    return SimpleDateFormat(pattern, Locale("es", "PE")).apply {
        timeZone = TimeZone.getTimeZone("GMT-5")
    }.format(date)
}

fun Activity.hasGoogleServices(): Boolean {
    val googleApi = GoogleApiAvailability.getInstance()
    val status = googleApi.isGooglePlayServicesAvailable(this)
    if (status != ConnectionResult.SUCCESS) {
        if (googleApi.isUserResolvableError(status))
            googleApi.getErrorDialog(this, status, 2404)?.show()
    }
    return status == ConnectionResult.SUCCESS
}