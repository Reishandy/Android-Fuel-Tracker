package id.reishandy.fueltracker.helper

import android.content.Context
import android.widget.Toast
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun showToast(context: Context, message: String, isLong: Boolean = false) {
    Toast.makeText(
        context,
        message,
        if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    ).show()
}

fun convertMillisToDate(millis: Long): String {
    val date = Date(millis)
    val format = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
    return format.format(date)
}

fun formatNumber(value: Double, maxFractionDigits: Int = 0): String {
    return NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        maximumFractionDigits = maxFractionDigits
    }.format(value)
}

fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(value)
}