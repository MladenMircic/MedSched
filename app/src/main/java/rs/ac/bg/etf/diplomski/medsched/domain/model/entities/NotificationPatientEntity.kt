package rs.ac.bg.etf.diplomski.medsched.domain.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens.NotificationType

@Entity
data class NotificationPatientEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateOfAction: LocalDate,
    val timeOfAction: LocalTime,
    val dateNotified: LocalDate,
    val timeNotified: LocalTime,
    val doctorName: String,
    val type: NotificationType,
    var read: Boolean = false
)