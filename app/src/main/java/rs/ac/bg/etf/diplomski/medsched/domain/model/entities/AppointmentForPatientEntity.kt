package rs.ac.bg.etf.diplomski.medsched.domain.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForPatient

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
    @ColumnInfo(name = "exam_id") val examId: Int,
    val confirmed: Boolean,
    @ColumnInfo(name = "cancelled_by") val cancelledBy: Int
) {
    fun toAppointmentForPatient(): AppointmentForPatient {
        return AppointmentForPatient(
            doctorName = doctorName,
            doctorSpecializationId = doctorSpecializationId,
            appointment = Appointment(
                id = appointmentId,
                date = date,
                time = time,
                doctorId = doctorId,
                patientId = patientId,
                examId = examId,
                confirmed = confirmed,
                cancelledBy = cancelledBy
            )
        )
    }
}