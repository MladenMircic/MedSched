package rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.NotificationPatientEntity
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ClinicIdToNameMapUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ImageRequestUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.NotificationsUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.CancelAppointmentUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetAllAppointmentsForPatientUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.ScheduledAppointmentsEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens.NotificationType
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.ScheduledState
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.animated
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PatientScheduledViewModel @Inject constructor(
    private val imageRequestUseCase: ImageRequestUseCase,
    private val getAllAppointmentsForPatientUseCase: GetAllAppointmentsForPatientUseCase,
    private val cancelAppointmentUseCase: CancelAppointmentUseCase,
    private val clinicIdToNameMapUseCase: ClinicIdToNameMapUseCase,
    private val notificationsUseCase: NotificationsUseCase
) : ViewModel() {

    private val _scheduledState = MutableStateFlow(ScheduledState())
    val scheduledState = _scheduledState.asStateFlow()

    private val appointmentsWithDoctorFlow: Flow<List<AppointmentForPatient>> =
        getAllAppointmentsForPatientUseCase.appointmentWithDoctorFlow

    init {
        viewModelScope.launch {
            appointmentsWithDoctorFlow.collectLatest { appointmentWithDoctorList ->
                _scheduledState.value.animatedAppointmentForPatientList.clear(true)
                delay(500L)
                _scheduledState.update { state ->
                    state.copy(
                        animatedAppointmentForPatientList = mutableStateListOf<AppointmentForPatient>()
                            .also {
                                it.addAll(appointmentWithDoctorList)
                            }.animated,
                        isRefreshing = false,
                        isListEmpty = appointmentWithDoctorList.isEmpty()
                    )
                }
            }
        }
    }

    fun onEvent(scheduledAppointmentsEvent: ScheduledAppointmentsEvent) {
        when (scheduledAppointmentsEvent) {
            is ScheduledAppointmentsEvent.DoctorImageFetch -> {
                viewModelScope.launch(Dispatchers.IO) {
                    scheduledAppointmentsEvent.appointmentForPatient.doctorImageRequest =
                        imageRequestUseCase("/doctors/Doctor.png")
                }
            }
            is ScheduledAppointmentsEvent.SetAppointmentToDelete -> {
                _scheduledState.update {
                    it.copy(
                        appointmentToDelete = scheduledAppointmentsEvent.appointmentForPatient
                    )
                }
            }
            is ScheduledAppointmentsEvent.RefreshAppointments -> {
                refreshAppointments()
            }
            is ScheduledAppointmentsEvent.CancelAppointment -> {
                cancelAppointment()
            }
        }
    }

    private fun refreshAppointments() {
        _scheduledState.update {
            it.copy(
                isRefreshing = true
            )
        }
        viewModelScope.launch {
            delay(1000L)
            val response = getAllAppointmentsForPatientUseCase.fetchAllAppointmentsAndSaveInLocal()
            response.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _scheduledState.update {
                            it.copy(
                                isRefreshing = false
                            )
                        }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun cancelAppointment() = viewModelScope.launch {
        val appointmentForPatient = _scheduledState.value.appointmentToDelete!!
        val response = cancelAppointmentUseCase(
            appointmentForPatient.appointment.id
        )
        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _scheduledState.value.animatedAppointmentForPatientList.remove(
                        _scheduledState.value.appointmentToDelete!!
                    )
                    _scheduledState.update { state ->
                        val animatedList = state.animatedAppointmentForPatientList
                            .also {
                                it.remove(state.appointmentToDelete!!)
                            }
                        state.copy(
                            animatedAppointmentForPatientList = animatedList,
                            appointmentToDelete = null,
                            isListEmpty = animatedList.size == 0
                        )
                    }
                    val currentDateTime = Instant
                        .fromEpochMilliseconds(Date().time)
                        .toLocalDateTime(
                            TimeZone.currentSystemDefault()
                        )
                    notificationsUseCase.sendNotification(
                        NotificationPatientEntity(
                            dateOfAction = appointmentForPatient.appointment.date,
                            timeOfAction = appointmentForPatient.appointment.time,
                            dateNotified = currentDateTime.date,
                            timeNotified = currentDateTime.time,
                            doctorName = appointmentForPatient.doctorName,
                            type = NotificationType.CANCELLED
                        )
                    )
                }
                is Resource.Error -> {}
                is Resource.Loading -> {}
            }
        }
    }

    fun specializationIdToNameId(specializationId: Int): Int =
        clinicIdToNameMapUseCase.specializationIdToNameId(specializationId = specializationId)

    fun serviceIdToNameId(serviceId: Int): Int =
        clinicIdToNameMapUseCase.serviceIdToNameId(serviceId = serviceId)
}