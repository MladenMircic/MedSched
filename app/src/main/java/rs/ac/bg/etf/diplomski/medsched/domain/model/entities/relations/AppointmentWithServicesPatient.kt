package rs.ac.bg.etf.diplomski.medsched.domain.model.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentForPatientEntity
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentServicePatientEntity

data class AppointmentWithServicesPatient(
    @Embedded val appointmentForPatientEntity: AppointmentForPatientEntity,
    @Relation(
        parentColumn = "appointment_id",
        entityColumn = "appointment_id"
    )
    val services: List<AppointmentServicePatientEntity>
) {
    fun toAppointmentForPatient(): AppointmentForPatient {
        return AppointmentForPatient(
            doctorName = appointmentForPatientEntity.doctorName,
            doctorSpecializationId = appointmentForPatientEntity.doctorSpecializationId,
            appointment = Appointment(
                id = appointmentForPatientEntity.appointmentId,
                date = appointmentForPatientEntity.date,
                time = appointmentForPatientEntity.time,
                doctorId = appointmentForPatientEntity.doctorId,
                patientId = appointmentForPatientEntity.patientId,
                confirmed = appointmentForPatientEntity.confirmed,
                services = services.map { it.toService() },
                cancelledBy = appointmentForPatientEntity.cancelledBy
            )
        )
    }
}