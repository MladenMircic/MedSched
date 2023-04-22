package rs.ac.bg.etf.diplomski.medsched.domain.model.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForDoctor
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentForDoctorEntity
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentServicePatientEntity

data class AppointmentWithServicesDoctor(
    @Embedded val appointmentForDoctorEntity: AppointmentForDoctorEntity,
    @Relation(
        parentColumn = "appointment_id",
        entityColumn = "appointment_id"
    )
    val services: List<AppointmentServicePatientEntity>
) {
    fun toAppointmentForDoctor(): AppointmentForDoctor {
        return AppointmentForDoctor(
            patientName = appointmentForDoctorEntity.patientName,
            appointment = Appointment(
                id = appointmentForDoctorEntity.appointmentId,
                date = appointmentForDoctorEntity.date,
                time = appointmentForDoctorEntity.time,
                doctorId = appointmentForDoctorEntity.doctorId,
                patientId = appointmentForDoctorEntity.patientId,
                confirmed = appointmentForDoctorEntity.confirmed,
                services = services.map { it.toService() },
                cancelledBy = appointmentForDoctorEntity.cancelledBy
            )
        )
    }
}