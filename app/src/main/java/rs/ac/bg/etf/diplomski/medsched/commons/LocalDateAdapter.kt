package rs.ac.bg.etf.diplomski.medsched.commons

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import kotlinx.datetime.LocalDate

class LocalDateAdapter {

    @FromJson
    fun getLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString)
    }

    @ToJson
    fun toLocalDate(localDate: LocalDate): String {
        return localDate.toString()
    }
}