package rs.ac.bg.etf.diplomski.medsched.domain.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForDoctor

@Entity
data class AppointmentForDoctorEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val time: LocalTime,
    @ColumnInfo(name = "appointment_id") val appointmentId: Int,
    @ColumnInfo(name = "doctor_id") val doctorId: Int,
    @ColumnInfo(name = "patient_id") val patientId: Int,
    @ColumnInfo(name = "patient_name") val patientName: String,
    @ColumnInfo(name = "exam_id") val examId: Int,
    val confirmed: Boolean,
    @ColumnInfo(name = "cancelled_by") val cancelledBy: Int
) {
    fun toAppointmentForDoctor(): AppointmentForDoctor {
        return AppointmentForDoctor(
            patientName = patientName,
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