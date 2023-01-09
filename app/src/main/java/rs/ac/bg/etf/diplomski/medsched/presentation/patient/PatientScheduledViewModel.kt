package rs.ac.bg.etf.diplomski.medsched.presentation.patient

import android.util.Log
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
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ImageRequestUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.CancelAppointmentUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetAllScheduledUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.ScheduledState
import javax.inject.Inject

@HiltViewModel
class PatientScheduledViewModel @Inject constructor(
    private val imageRequestUseCase: ImageRequestUseCase,
    private val getAllScheduledUseCase: GetAllScheduledUseCase,
    private val cancelAppointmentUseCase: CancelAppointmentUseCase
) : ViewModel() {

    private val _scheduledState = MutableStateFlow(ScheduledState())
    val scheduledState = _scheduledState.asStateFlow()

    init {
        getAppointments()
    }

    fun refreshAppointments() {
        _scheduledState.update {
            it.copy(isRefreshing = true)
        }
        getAppointments(visibility = true)
    }

    private fun getAppointments(visibility: Boolean = false) = viewModelScope.launch {
        delay(1000L)
        val response = getAllScheduledUseCase()

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data!!.forEach { scheduled ->
                        scheduled.doctorImageRequest = withContext(Dispatchers.IO) {
                            imageRequestUseCase("/doctors/Doctor.png")
                        }
                    }
                    _scheduledState.update { it.copy(
                        scheduledList = resource.data,
                        alreadyRevealed = resource.data.map { visibility },
                        isRefreshing = false
                    ) }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

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

    fun deleteCancelledAppointment() {
        val deleteIndex = _scheduledState.value.lastDeleted
        _scheduledState.update {
            it.copy(
                scheduledList = it.scheduledList.toMutableList().also { scheduled ->
                    scheduled.removeAt(deleteIndex!!)
                },
                alreadyRevealed = it.alreadyRevealed.toMutableList().also { revealed ->
                    revealed.removeAt(deleteIndex!!)
                },
                lastDeleted = null
            )
        }
        _scheduledState.value.scheduledList.forEach {
            Log.d("TESTIRANJE", "${it.appointment.id}")
        }
    }

    fun cancelAppointment(deleteIndex: Int, appointmentId: Int) = viewModelScope.launch {
        _scheduledState.update { it.copy(deletingIndex = deleteIndex) }
        val response = cancelAppointmentUseCase(appointmentId)

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _scheduledState.update { it.copy(
                        deletingIndex = null,
                        lastDeleted = deleteIndex
                    ) }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        }
    }
}