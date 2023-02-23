package rs.ac.bg.etf.diplomski.medsched.presentation.patient.events

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForPatient

sealed class ScheduledAppointmentsEvent {
    data class DoctorImageFetch(
        val appointmentForPatient: AppointmentForPatient
    ): ScheduledAppointmentsEvent()
    data class SetAppointmentToDelete(
        val appointmentForPatient: AppointmentForPatient
    ): ScheduledAppointmentsEvent()
    object RefreshAppointments: ScheduledAppointmentsEvent()
    object CancelAppointment: ScheduledAppointmentsEvent()
}