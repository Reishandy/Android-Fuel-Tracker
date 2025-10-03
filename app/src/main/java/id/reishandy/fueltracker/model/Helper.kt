package id.reishandy.fueltracker.model

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, message: String, isLong: Boolean = false) {
    Toast.makeText(
        context,
        message,
        if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    ).show()
}
