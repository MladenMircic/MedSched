package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentServicePatientEntity

data class Service(
    val id: Int,
    val name: String,
    val categoryId: Int
) {
    fun toAppointmentServicePatientEntity(appointmentId: Int): AppointmentServicePatientEntity {
        return AppointmentServicePatientEntity(
            appointmentId = appointmentId,
            serviceId = id
        )
    }
}