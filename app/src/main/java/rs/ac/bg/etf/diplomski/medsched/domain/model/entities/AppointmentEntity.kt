package rs.ac.bg.etf.diplomski.medsched.domain.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentWithDoctor

@Entity
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val time: LocalTime,
    @ColumnInfo(name = "appointment_id") val appointmentId: Int,
    @ColumnInfo(name = "doctor_id") val doctorId: Int,
    @ColumnInfo(name = "doctor_name") val doctorName: String,
    @ColumnInfo(name = "doctor_specialization_id") val doctorSpecializationId: Int,
    @ColumnInfo(name = "patient_id") val patientId: Int,
    @ColumnInfo(name = "exam_id") val examId: Int
) {
    fun toAppointmentWithDoctor(): AppointmentWithDoctor {
        return AppointmentWithDoctor(
            doctorName = doctorName,
            doctorSpecializationId = doctorSpecializationId,
            appointment = Appointment(
                id = appointmentId,
                date = date,
                time = time,
                doctorId = doctorId,
                patientId = patientId,
                examId = examId
            )
        )
    }
}