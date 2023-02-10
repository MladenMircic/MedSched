package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForPatient

data class ScheduledState(
    val appointmentForPatientList: List<AppointmentForPatient> = listOf(),
    val alreadyRevealed: List<Boolean> = listOf(),
    val deletedList: List<AppointmentForPatient> = listOf(),
    val revealNew: Boolean = false,
    val message: String? = null,
    val appointmentToDelete: AppointmentForPatient? = null,
    val lastAppointmentDeleted: AppointmentForPatient? = null,
    val isRefreshing: Boolean = false
)