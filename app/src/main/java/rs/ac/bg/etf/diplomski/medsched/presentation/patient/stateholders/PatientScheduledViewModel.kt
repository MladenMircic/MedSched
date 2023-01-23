package rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Scheduled
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ClinicIdToNameMapUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ImageRequestUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.CancelAppointmentUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetAllScheduledUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.SaveAppointmentsUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.ScheduledState
import javax.inject.Inject

@HiltViewModel
class PatientScheduledViewModel @Inject constructor(
    private val imageRequestUseCase: ImageRequestUseCase,
    private val getAllScheduledUseCase: GetAllScheduledUseCase,
    private val cancelAppointmentUseCase: CancelAppointmentUseCase,
    private val clinicIdToNameMapUseCase: ClinicIdToNameMapUseCase,
    private val saveAppointmentsUseCase: SaveAppointmentsUseCase
) : ViewModel() {

    private val _scheduledState = MutableStateFlow(ScheduledState())
    val scheduledState = _scheduledState.asStateFlow()

    init {
        getAppointments()
    }

    fun refreshAppointments() {
        _scheduledState.update {
            it.copy(
                alreadyRevealed = it.alreadyRevealed.map { false }
            )
        }
        getAppointments()
    }

    private fun getAppointments() = viewModelScope.launch {
        val response = getAllScheduledUseCase()

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data!!.forEach { scheduled ->
                        scheduled.doctorImageRequest = withContext(Dispatchers.IO) {
                            imageRequestUseCase("/doctors/Doctor.png")
                        }
                    }
                    saveAppointmentsUseCase(resource.data.map { it.appointment })
                    delay(800L)
                    _scheduledState.update { it.copy(
                        scheduledList = resource.data,
                        alreadyRevealed = resource.data.map { false },
                        deletedList = listOf(),
                        isRefreshing = false,
                        revealNew = true
                    ) }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    _scheduledState.update {
                        it.copy(isRefreshing = true)
                    }
                }
            }
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

    fun markAppointmentDeleted(scheduled: Scheduled) {
        _scheduledState.update {
            it.copy(
                deletedList = it.deletedList.toMutableList().also { deletedList ->
                    deletedList.add(scheduled)
                },
                lastDeleted = null
            )
        }
    }

    fun cancelAppointment(deleteIndex: Int) = viewModelScope.launch {

        val response = cancelAppointmentUseCase(
            _scheduledState.value.scheduledList[deleteIndex].appointment.id
        )
        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _scheduledState.update { it.copy(
                        scheduleIndexToDelete = null,
                        lastDeleted = deleteIndex
                    ) }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    _scheduledState.update { it.copy(scheduleIndexToDelete = deleteIndex) }
                }
            }
        }
    }

    fun specializationIdToNameId(specializationId: Int): Int =
        clinicIdToNameMapUseCase.specializationIdToNameId(specializationId)
}