package rs.ac.bg.etf.diplomski.medsched.data.local

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class DateTimeConverters {

    @TypeConverter
    fun dateFromTimestamp(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun timeFromTimestamp(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(value) }
    }

    @TypeConverter
    fun timestampFromDate(localDate: LocalDate?): String? {
        return localDate?.toString()
    }

    @TypeConverter
    fun timestampFromTime(localTime: LocalTime?): String? {
        return localTime?.toString()
    }
}