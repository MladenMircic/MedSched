package rs.ac.bg.etf.diplomski.medsched.commons

import java.util.*

class CustomDateFormatter {
    companion object {
        fun dateAsString(day: Int, month: String, year: Int): String {
            return String.format(
                "%s %d, %d",
                month.lowercase()
                    .replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    }
                    .substring(0, 3),
                day,
                year
            )
        }

        fun timeAsString(hour: Int, minute: Int): String {
            return String.format(
                format = "%02d:%02d",
                hour,
                minute
            )
        }
    }
}