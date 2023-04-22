package rs.ac.bg.etf.diplomski.medsched.domain.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Entity
data class AppointmentForPatientEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val time: LocalTime,
    @ColumnInfo(name = "appointment_id") val appointmentId: Int,
    @ColumnInfo(name = "doctor_id") val doctorId: String,
    @ColumnInfo(name = "doctor_name") val doctorName: String,
    @ColumnInfo(name = "doctor_specialization_id") val doctorSpecializationId: Int,
    @ColumnInfo(name = "patient_id") val patientId: String,
    val confirmed: Boolean,
    @ColumnInfo(name = "cancelled_by") val cancelledBy: Int
)