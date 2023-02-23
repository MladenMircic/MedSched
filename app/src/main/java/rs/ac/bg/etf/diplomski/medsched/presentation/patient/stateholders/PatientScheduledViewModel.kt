package rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ClinicIdToNameMapUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ImageRequestUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.CancelAppointmentUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetAllAppointmentsForPatientUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.ScheduledAppointmentsEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.ScheduledState
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.animated
import javax.inject.Inject

@HiltViewModel
class PatientScheduledViewModel @Inject constructor(
    private val imageRequestUseCase: ImageRequestUseCase,
    private val getAllAppointmentsForPatientUseCase: GetAllAppointmentsForPatientUseCase,
    private val cancelAppointmentUseCase: CancelAppointmentUseCase,
    private val clinicIdToNameMapUseCase: ClinicIdToNameMapUseCase,
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
                        isRefreshing = false
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
        val response = cancelAppointmentUseCase(
            _scheduledState.value.appointmentToDelete!!.appointment.id
        )
        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _scheduledState.value.animatedAppointmentForPatientList.remove(
                        _scheduledState.value.appointmentToDelete!!
                    )
                    _scheduledState.update { state ->
                        state.copy(
                            animatedAppointmentForPatientList = state.animatedAppointmentForPatientList
                                .also {
                                    it.remove(state.appointmentToDelete!!)
                                },
                            appointmentToDelete = null
                        )
                    }
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