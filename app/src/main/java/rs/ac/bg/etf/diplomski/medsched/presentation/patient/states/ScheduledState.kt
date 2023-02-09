package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentWithDoctor

data class ScheduledState(
    val appointmentWithDoctorList: List<AppointmentWithDoctor> = listOf(),
    val alreadyRevealed: List<Boolean> = listOf(),
    val deletedList: List<AppointmentWithDoctor> = listOf(),
    val revealNew: Boolean = false,
    val message: String? = null,
    val appointmentToDelete: AppointmentWithDoctor? = null,
    val lastAppointmentDeleted: AppointmentWithDoctor? = null,
    val isRefreshing: Boolean = false
)