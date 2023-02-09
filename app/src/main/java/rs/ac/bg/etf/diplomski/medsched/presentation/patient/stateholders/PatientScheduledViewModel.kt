package rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentWithDoctor
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ClinicIdToNameMapUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ImageRequestUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.CancelAppointmentUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetAllAppointmentsUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.ScheduledState
import javax.inject.Inject

@HiltViewModel
class PatientScheduledViewModel @Inject constructor(
    private val imageRequestUseCase: ImageRequestUseCase,
    private val getAllAppointmentsUseCase: GetAllAppointmentsUseCase,
    private val cancelAppointmentUseCase: CancelAppointmentUseCase,
    private val clinicIdToNameMapUseCase: ClinicIdToNameMapUseCase,
) : ViewModel() {

    private val _scheduledState = MutableStateFlow(ScheduledState())
    val scheduledState = _scheduledState.asStateFlow()

    val appointmentsWithDoctorFlow: Flow<List<AppointmentWithDoctor>> =
        getAllAppointmentsUseCase.appointmentWithDoctorFlow

    init {
        viewModelScope.launch {
            appointmentsWithDoctorFlow.collectLatest { appointmentWithDoctorList ->
                _scheduledState.update {
                    it.copy(
                        alreadyRevealed = appointmentWithDoctorList.map { false },
                        deletedList = listOf(),
                        isRefreshing = false,
                        revealNew = true
                    )
                }
            }
        }
    }

    fun refreshAppointments() {
        _scheduledState.update {
            it.copy(
                alreadyRevealed = it.alreadyRevealed.map { false },
                isRefreshing = true
            )
        }
        viewModelScope.launch {
            delay(1000L)
            getAllAppointmentsUseCase.fetchAllAppointmentsAndSaveInLocal()
        }
    }

    fun fetchDoctorImageForAppointment(
        appointmentWithDoctor: AppointmentWithDoctor
    ) = viewModelScope.launch {
        appointmentWithDoctor.doctorImageRequest = withContext(Dispatchers.IO) {
            imageRequestUseCase("/doctors/Doctor.png")
        }
    }

    fun toggleRevealed(index: Int) {
        _scheduledState.update {
            it.copy(
                alreadyRevealed = it.alreadyRevealed.toMutableList().also { revealed ->
                    revealed[index] = !revealed[index]
                }
            )
        }
    }

    fun triggerRevealForIndex(index: Int) {
        if (_scheduledState.value.alreadyRevealed.size <= index) return

        _scheduledState.update {
            it.copy(
                alreadyRevealed = it.alreadyRevealed.toMutableList().also { revealed ->
                    revealed[index] = true
                }
            )
        }
    }

    fun setRevealNew(revealNew: Boolean) {
        _scheduledState.update {
            it.copy(revealNew = revealNew)
        }
    }

    fun markAppointmentDeleted(appointmentWithDoctor: AppointmentWithDoctor) {
        _scheduledState.update {
            it.copy(
                deletedList = it.deletedList.toMutableList().also { deletedList ->
                    deletedList.add(appointmentWithDoctor)
                },
                lastAppointmentDeleted = null
            )
        }
    }

    fun cancelAppointment(appointmentToDelete: AppointmentWithDoctor) = viewModelScope.launch {
        val response = cancelAppointmentUseCase(appointmentToDelete.appointment.id)
        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _scheduledState.update { it.copy(
                        appointmentToDelete = null,
                        lastAppointmentDeleted = appointmentToDelete
                    ) }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    _scheduledState.update { it.copy(appointmentToDelete = appointmentToDelete) }
                }
            }
        }
    }

    fun specializationIdToNameId(specializationId: Int): Int =
        clinicIdToNameMapUseCase.specializationIdToNameId(specializationId = specializationId)

    fun serviceIdToNameId(serviceId: Int): Int =
        clinicIdToNameMapUseCase.serviceIdToNameId(serviceId = serviceId)
}