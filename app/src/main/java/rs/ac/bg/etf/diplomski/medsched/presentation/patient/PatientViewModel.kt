package rs.ac.bg.etf.diplomski.medsched.presentation.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.GetDoctorsUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.GetServicesUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ImageRequestUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.PatientEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.AppointmentState
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.PatientState
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    patientRepository: PatientRepository,
    private val getServicesUseCase: GetServicesUseCase,
    private val imageRequestUseCase: ImageRequestUseCase,
    private val getDoctorsUseCase: GetDoctorsUseCase
): ViewModel() {

    val userFlow = patientRepository.user

    private val _patientState = MutableStateFlow(PatientState())
    val patientState = _patientState.asStateFlow()

    private val _appointmentState = MutableStateFlow(AppointmentState())
    val appointmentState = _appointmentState.asStateFlow()

    init {
        getAllServices()
        getDoctors()
    }

    fun onEvent(patientEvent: PatientEvent) {
        when (patientEvent) {
            is PatientEvent.SelectService -> {
                _patientState.update { it.copy(selectedService = patientEvent.index) }
            }
            is PatientEvent.SearchTextChange -> {
                _patientState.update { it.copy(searchKeyWord = patientEvent.text) }
            }
            is PatientEvent.SearchForDoctor -> {

            }
            is PatientEvent.SetAppointmentDate -> {
                _appointmentState.update {
                    it.copy(selectedDate = patientEvent.date ?: LocalDate.now())
                }
            }
            is PatientEvent.SelectDoctor -> {
                _patientState.update { it.copy(selectedDoctor = patientEvent.index) }
            }
            is PatientEvent.GetAllServices -> {
                getAllServices()
            }
        }
    }

    private fun getAllServices() = viewModelScope.launch {
        val response = getServicesUseCase()

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data.let {
                        _patientState.update { it.copy(serviceList = resource.data!!) }
                    }
                    for (service in resource.data!!) {
                        service.imageRequest = withContext(Dispatchers.IO) {
                            imageRequestUseCase("/services/${service.name}.png")
                        }
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        }
    }

    fun getSelectedDoctor(): DoctorForPatient =
        _patientState.value.doctorList[_patientState.value.selectedDoctor ?: 0]

    private fun getDoctors() = viewModelScope.launch {
        val patientState = _patientState.value
        val serviceCategory: String = if (patientState.selectedService != null)
            patientState.serviceList[patientState.selectedService].name
        else ""
        val response = getDoctorsUseCase(serviceCategory)

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    for (doctor in resource.data!!) {
                        doctor.imageRequest = withContext(Dispatchers.IO) {
                            imageRequestUseCase("/doctors/Doctor.png")
                        }
                    }
                    _patientState.update {
                        it.copy(
                            doctorList = resource.data,
                            doctorsLoading = false
                        )
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    _patientState.update { it.copy(doctorsLoading = true) }
                }
            }
        }
    }

}