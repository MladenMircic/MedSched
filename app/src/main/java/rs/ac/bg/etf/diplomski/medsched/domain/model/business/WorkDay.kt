package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

data class WorkDay(
    val dayOfWeek: DayOfWeek,
    var workHours: List<LocalTime> = listOf()
)