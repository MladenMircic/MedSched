package rs.ac.bg.etf.diplomski.medsched.commons

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import kotlinx.datetime.LocalTime

class LocalTimeAdapter {

    @FromJson
    fun getLocalTime(timeString: String): LocalTime {
        return LocalTime.parse(timeString)
    }

    @ToJson
    fun toLocalTime(localTime: LocalTime): String {
        return localTime.toString()
    }
}