package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import androidx.compose.runtime.mutableStateListOf
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForPatient
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.VisibilityList
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.animated

data class ScheduledState(
    val animatedAppointmentForPatientList: VisibilityList<AppointmentForPatient> =
        mutableStateListOf<AppointmentForPatient>().animated,
    val appointmentToDelete: AppointmentForPatient? = null,
    val message: String? = null,
    val isRefreshing: Boolean = false,
    val isListEmpty: Boolean = false
)