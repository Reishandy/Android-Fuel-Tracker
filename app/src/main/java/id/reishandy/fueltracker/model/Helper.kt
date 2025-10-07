package id.reishandy.fueltracker.model

import android.content.Context
import android.widget.Toast
import java.util.Locale

fun showToast(context: Context, message: String, isLong: Boolean = false) {
    Toast.makeText(
        context,
        message,
        if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    ).show()
}

fun convertMillisToDate(millis: Long): String {
    val date = java.util.Date(millis)
    val format = java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
    return format.format(date)
}
