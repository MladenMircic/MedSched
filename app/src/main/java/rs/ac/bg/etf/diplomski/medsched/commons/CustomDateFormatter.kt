package rs.ac.bg.etf.diplomski.medsched.commons

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.util.*

class CustomDateFormatter {
    companion object {
        fun dateAsString(date: LocalDate, omitYear: Boolean = false): String {
            return if (!omitYear)
                String.format(
                    "%s %d, %d",
                    date.month.name.lowercase()
                        .replaceFirstChar {
                            if (it.isLowerCase())
                                it.titlecase(Locale.getDefault())
                            else
                                it.toString()
                        }
                        .substring(0, 3),
                    date.dayOfMonth,
                    date.year
                )
            else
                String.format(
                    "%s %d",
                    date.month.name.lowercase()
                        .replaceFirstChar {
                            if (it.isLowerCase())
                                it.titlecase(Locale.getDefault())
                            else
                                it.toString()
                        }
                        .substring(0, 3),
                    date.dayOfMonth
                )
        }

        fun timeAsString(time: LocalTime): String {
            return String.format(
                format = "%02d:%02d",
                time.hour,
                time.minute
            )
        }
    }
}